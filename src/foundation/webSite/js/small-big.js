		$(document).ready(function() {
			$("#example1-1").imgbox();

			$("#example1-2").imgbox({
			    'zoomOpacity'	: true,
				'alignment'		: 'center'
			});

			$("#example1-3").imgbox({
				'speedIn'		: 0,
				'speedOut'		: 0
			});

			$("#example2-1, #example2-2").imgbox({
				'speedIn'		: 0,
				'speedOut'		: 0,
				'alignment'		: 'center',
				'overlayShow'	: true,
				'allowMultiple'	: false
			});
		});