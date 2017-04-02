var express = require('express');
// var fs = require('fs');
var mq = require('../lib/mq');
//通过读取配置文件的方式配置文件保存路径
var config = require('config');
var path = config.get('upload.path');

//解决上传文件的问题
var multer  = require('multer');
var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, path)
  },
  filename: function (req, file, cb) {
    cb(null, file.fieldname + '-' + Date.now() + '.pdf')
  }
});

var upload = multer({ storage: storage }).single('pdf');
var router = express.Router();


router.post('/', function(req, res, next) {
	upload(req,res,function(err) {
		if(err) {
			console.log(err);
			res.render('error');
		}
		try{
			var file = req.file;
			if(file == null)
				res.send('Blank file');
			else{
				var msg = req.ip + " at " + Date.now() + " uploads " + file.originalname + " to " + file.path
                    + ", size: " + file.size + " B";
				console.log(msg);
				var path = file.path;
				mq.push(path);
				// console.log(file.originalname);
				res.type('html');
				res.set('Content-Type', 'text/plain');
				res.status(200).send(msg);
				// var ajaxTest={
				//     tips:"you are not alone"
				// };
				// res.send(ajaxTest);
			}
		}catch(err) {
			console.log(err);
			res.render('error');
		}
		/*file对象
		{ 
		  fieldname: 'pdf',
		  originalname: '07879103.pdf',
		  encoding: '7bit',
		  mimetype: 'application/pdf',
		  destination: 'uploads/',
		  filename: 'pdf-1490858375972.pdf',
		  path: 'uploads/pdf-1490858375972.pdf',
		  size: 681975 
		}
		*/
		// res.send(file.originalname);
	});
});

router.get('/get', function (req, res, next) {
    var msg = 'fuck u at ' + Date.now();
    mq.push(msg);
    res.send(msg);
});

router.get('/', function(req, res, next) {
	mq.get(res);
});

module.exports = router;