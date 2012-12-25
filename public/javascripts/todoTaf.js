/**
 * Select current menu item
 */
function selectMenu(menuItem) {
	$("ul.nav li").removeClass("active");
	$("#" + menuItem).addClass("active");
}

/**
 * Initializes project list view
 */
function initProjectView() {
	selectMenu("projectsMenu");
}

/**
 * Initializes task list view
 */
function initTaskView() {
	// Menu stuff
	selectMenu("tasksMenu");

	// Popup stuff
	$("#createTaskForm").dialog({
		autoOpen: false,
		modal: true,
		buttons: {
			"Create task": function() {
				$(this).children(":first").submit();
			}
		}
	});
	$("#createTask").click(function() {
		$("#createTaskForm").dialog("open");
	});

	// Datatable stuff
	$("#taskTable").dataTable({
		"sDom": 'T<"clear">lfrtip',
		"oTableTools": {
			"aButtons": [],
			"sRowSelect": "single" 
		}
	});
	$("#taskTable tr").click(function(event) {
		var taskId = $(this).data("taskid");
		console.log("taskId: " + taskId);
	});
	$(this).keyup(function(event) {
		if (event.which == 27) {
			var taskTable = TableTools.fnGetMasters()[0];
			taskTable.fnSelectNone();
		}
	});

	// input type date causes different browser behaviors
	$("input.dateField").datepicker({
		dateFormat: "dd/mm/yy"
	});
}