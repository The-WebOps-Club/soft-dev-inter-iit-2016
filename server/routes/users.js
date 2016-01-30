var express = require('express');
var router = express.Router();
var User = require('../models').User;
var util = require('./util');
var settings = require('./settings');
var _ = require('underscore');

function handleError(res, err) {
  return res.status(500).send(err);
}

// Creates user
router.post('/create', function(req, res) {
  User.findOne({username: req.body.username}, function(err, _user) {
    /**
     * req.body - username, phoneNumber, gcmId
     */
    debugger;
    if(_user) {
      res.status(204).send('Invalid username');
    }
    else {
      var user = new User(req.body);
      user.save();
      res.status(201).json(user);
    }
  });
});

// Returns all users
router.get('/', function(req, res) {
  User.find({}, function(err, users) {
    if (err) { return handleError(res, err); }
    res.status(200).json(users);
  });
});

// Returns particular user
router.get('/:id', function(req, res) {
  User.findById(req.params.id, function(err, user) {
    if (err) { return handleError(res, err); }
    if(!user) { return res.status(404).send('Not Found'); }
    res.status(200).json(user);
  });
});

// Adds a user as a contact
router.post('/:id/contacts/add', function(req, res) {
  User.findById(req.params.id, function(err, user) {
    if (err) { return handleError(res, err); }
    if(!user) { return res.status(404).send('Not Found'); }

    user.contacts = _.uniq(user.contacts.concat(req.body.contacts));
    user.markModified("contacts");
    user.save();
    res.status(200).send('OK');
  });
});

// Updates GCM Id
router.post('/:id/gcmId', function(req, res) {
  /**
   * req.body.gcmId - GCM Id of the user
   */
  User.findById(req.params.id, function(err, user) {
    if (err) { return handleError(res, err); }
    if(!user) { return res.status(404).send('Not Found'); }

    user.gcmId = req.body.gcmId;
    user.save();
    res.status(200).send('OK');
  });
});

// Filters users by phone
router.get('/filter/phone', function(req, res) {
  var phoneNumbers = req.query.phoneNumbers;
  User.find({phoneNumber:{
    $in: phoneNumbers
  }}, function(err, users) {
    if (err) { return handleError(res, err); }
    res.status(200).json(users);
  });
});

// Makes a request through a GCM push for all contacts
router.post('/alert/request', function(req, res) {
  /**
   * req.body.users - List of contacts
   * req.body.userId - User's ID
   * req.body.location - User's location like : {'lat':43.2, 'lng':31.3}
   * req.body.message - Message to be delivered
   */
	debugger;
console.log(req.body);
  var data = {type:"REQUEST", fromUserId:req.body.userId, fromUserLocation: {lat:req.body.lat,lng:req.body.lng},
              radius: settings.general.NOTIFICATION_RADIUS};
  User.find({_id:{
    $in: req.body.users
  }}, function(err, users) {
	debugger;
    if (err) { return handleError(res, err); }
    User.findById(req.body.userId,function(err,user){
	    data.fromUser = user;
        if (user.details && user.details.radius) data.radius = user.details.radius;
	    util.gcmNotify(users, data);
	    res.status(200).json({status: 'OK', data:data});
    });
  });
});

// router.post('/alert/locate', function(req, res) {
//   var data = {type:"LOCATE", from:req.body.userId, fromLocation: req.body.location};
//   User.findById(req.body.userId, function(err, user) {
//     if (err) { return handleError(res, err); }
//     user.details.location = data.fromLocation;
//     util.gcmNotify([user], data);
//     res.status(200).send('OK');
//   });
// });

// Sends a GCM push to the user who needs help
router.post('/alert/accept', function(req, res) {
  /**
   * req.body.userId - User's ID
   * req.body.location - User's location like : {'lat':43.2, 'lng':31.3}
   */
  var data = {type:"ACCEPT", fromUserId:req.body.userId, fromUserLocation: req.body.location,
              radius: settings.general.NOTIFICATION_RADIUS};
  User.findById(req.body.userId, function(err, user) {
    if (err) { return handleError(res, err); }
    util.gcmNotify([user], data);
    res.status(200).send('OK');
  });
});

// Updates location of the user
router.post('/:id/location', function(req, res) {
  User.findById(req.params.id, function(err, user) {
    if(!user.details) {
      user.details = {};
    }
    user.details.location = req.body.location;
    user.markModified("details");
    user.save();
    res.status(200).send('OK');
  });
});

// update user defined radius
router.post('/:id/radius', function(req, res) {
  /*
   *    send radius, id in post sets details.radius to radius
   *    need to sanitize radius
   */
  User.findById(req.params.id, function(err, user) {
    if(!user.details) {
      user.details = {};
    }
    user.details.radius = req.body.radius;
    user.markModified("details");
    user.save();
    res.status(200).send('OK');
  });
});

// Returns location of the user
router.get('/:id/location', function(req, res) {
  User.findById(req.params.id, function(err, user) {
    if(user.details) {
       var location = user.details.location;
       res.status(200).json(location);
    }
    else {
       res.status(200).send('No location.');
    }
  });
});

module.exports = router;
