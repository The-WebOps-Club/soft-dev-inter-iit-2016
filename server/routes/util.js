var settings = require('./settings');
var gcm = require('node-gcm-service');

var gcmNotify = function(users, data){
    var regIds = [];
    users.forEach(function(current, pos, array) {
        var gcmId = current.gcmId;
        regIds.push(gcmId);
    });
    var message = new gcm.Message({
        collapse_key: 'alerto',
        delay_while_idle: true,
        data: data
    });
    var sender = new gcm.Sender();
    sender.setAPIKey(settings.gcm.apiKey);
    sender.sendMessage(message.toString(), regIds, true, function (err, res) {
        console.log(res);
	console.log("Sent to " + regIds);
        if(err) console.log(err);
    });
}

module.exports = {
  gcmNotify: gcmNotify
}
