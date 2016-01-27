var express = require('express');
var router = express.Router();
var User = require('../models').User;
var util = require('./util');
var _ = require('underscore');

function handleError(res, err) {
  return res.status(500).send(err);
}

router.post('/create', function(req, res) {
  User.findOne({username: req.body.username}, function(err, _user) {
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

router.get('/:id', function(req, res) {
  User.findById(req.params.id, function(err, user) {
    if (err) { return handleError(res, err); }
    if(!user) { return res.status(404).send('Not Found'); }
    res.status(200).json(user);
  });
});

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

router.get('/filter/phone', function(req, res) {
  var phoneNumbers = req.query.phoneNumbers;
  User.find({phoneNumber:{
    $in: phoneNumbers
  }}, function(err, users) {
    if (err) { return handleError(res, err); }
    res.status(200).json(users);
  });
});

router.post('/alert/request', function(req, res) {
  // Sends GCM push notifications to all users
  var data = {type:"REQUEST", from:req.body.userId};
  User.find({_id:{
    $in: req.body.users
  }}, function(err, users) {
    if (err) { return handleError(res, err); }
    util.gcmNotify(users, data);
    res.status(200).send('OK');
  });
});

router.post('/alert/accept', function(req, res) {
  // Sends a GCM push to the user who needs help
  var data = {type:"ACCEPT", from:req.body.userId};
  User.findById(req.body.userId, function(err, user) {
    if (err) { return handleError(res, err); }
    util.gcmNotify([user], data);
    res.status(200).send('OK');
  });
});

router.post('/:id/location', function(req, res) {
  // Updates location of the user
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

router.get('/:id/location', function(req, res) {
  // Returns location of the user
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
