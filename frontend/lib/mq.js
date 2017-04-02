/**
 * Created by puyangsky on 17/3/31.
 */
var amqp = require('amqplib/callback_api');
var config = require('config');
var url = config.get('rabbit.url');
var queue = config.get('rabbit.queue');

console.log("connect to " + url);

function push(msg) {
    amqp.connect(url, function (err, conn) {       //  创建连接
        conn.createChannel(function (err, ch) {

            ch.assertQueue(queue, {durable: false});

            // ch.sendToQueue(queue, new Buffer(msg));    //   发送消息
            ch.sendToQueue(queue, new Buffer(msg, "utf-8"));    //   发送消息
            console.log("Send message:", msg);
        });
        setTimeout(function () {
            conn.close();
            // process.exit(0)
        }, 500);
    });
}

function get(res) {
    var amqp = require('amqplib/callback_api');
    var ret;
    amqp.connect(url, function(err, conn) {     //  创建连接
        conn.createChannel(function(err, ch) {
            ch.assertQueue(queue, {durable: false});
            ch.consume(queue, function(msg) {           //  接收消息
                console.log("get Message", msg.content.toString());
                ret = msg.content.toString();
                // res.send(ret);
            }, {noAck: true});
        });
    });
}

//导出接口
exports.push=push;
exports.get=get;

