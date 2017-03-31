/**
 * Created by puyangsky on 17/3/31.
 */
var amqp = require('amqplib/callback_api');

amqp.connect('amqp://localhost', function(err, conn) {       //  创建连接
    conn.createChannel(function(err, ch) {
        var q = 'test';
        var msg = 'Hello World!';

        ch.assertQueue(q, {durable: false});
        ch.sendToQueue(q, new Buffer(msg));    //   发送消息
        console.log("Send message:", msg);
    });
    setTimeout(function() { conn.close(); process.exit(0) }, 500);
});