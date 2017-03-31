/**
 * Created by puyangsky on 17/3/31.
 */
var amqp = require('amqplib/callback_api');
var config = require('config');
var url = config.get('rabbit.url');

console.log("connect to " + url);

function push(data) {
    amqp.connect(url, function (err, conn) {       //  创建连接
        conn.createChannel(function (err, ch) {
            var q = 'default';
            // var msg = 'Hello World!';

            ch.assertQueue(q, {durable: false});
            ch.sendToQueue(q, new Buffer(data));    //   发送消息
            console.log("Send message:", data);
        });
        setTimeout(function () {
            conn.close();
            // process.exit(0)
        }, 500);
    });
}


function get() {
    var amqp = require('amqplib/callback_api');

    amqp.connect(url, function(err, conn) {     //  创建连接
        conn.createChannel(function(err, ch) {
            var q = 'test';
            ch.assertQueue(q, {durable: false});
            ch.consume(q, function(msg) {           //  接收消息
                console.log("get Message", msg.content.toString());
            }, {noAck: true});
        });
    });
}

//导出接口
exports.push=push;
exports.get=get;

// get();

