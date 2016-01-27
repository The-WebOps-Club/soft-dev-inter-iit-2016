var mongoose = require("mongoose");

var Models = {};

var Schema = mongoose.Schema;
var ObjectId = Schema.ObjectId;

Models.User = mongoose.model('User', new Schema({
	username: String,
	dateCreated: Date,
	dateUpdated: Date,
  phoneNumber: String,
  gcmId: String,
  contacts: [{ type: Schema.Types.ObjectId, ref: 'User' }],
	details: {}
}));

module.exports = Models;
