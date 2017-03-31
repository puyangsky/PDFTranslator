var express = require('express');
var fs = require('fs');
var multer  = require('multer');
//var upload = multer({ dest: 'uploads/' });
var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'uploads/')
  },
  filename: function (req, file, cb) {
    cb(null, file.fieldname + '-' + Date.now() + '.pdf')
  }
});

var upload = multer({ storage: storage }).single('pdf')
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
				console.log(req.ip + " at " + Date.now() + " uploads " + file.originalname
					+ ", size: " + file.size + "B");
				console.log(file.originalname);
				res.type('html');
				res.set('Content-Type', 'text/plain');
				res.status(200).send(file.originalname);
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

router.get('/*', function(req, res, next) {
	var from = req.params[0];
	console.log(from);
	res.send(from);
	//console.log(req.body)
	//res.send('GET function');
});

module.exports = router;