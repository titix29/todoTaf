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

	// Create popup stuff
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
	
	// Edit popup stuff
	$("#updateTaskForm").hide();

	// Datatable stuff
	$("#taskTable").dataTable({
		"sDom": 'T<"clear">lfrtip',
		"oTableTools": {
			"aButtons": [],
			"sRowSelect": "single" 
		}
	});
	$("#taskTable tr").click(function(event) {
		var taskId = $(this).data("taskId");
		updateTask(taskId);
	});
	$(this).keyup(function(event) {
		if (event.which == 27) {
			// deselect all on ESC key
			var taskTable = TableTools.fnGetMasters()[0];
			taskTable.fnSelectNone();
			
			// hide update task form
			$("#updateTaskForm").hide();
		}
	});

	// input type date causes different browser behaviors
	$("input.dateField").datepicker({
		dateFormat: "dd/mm/yy"
	});
}

function updateTask(id) {
	// retrieve data
	$.getJSON("/tasks/" + id)
		.done(function(data) { loadUpdateTaskForm(data); })
		.fail(function(err) { alert(err); });
}

function loadUpdateTaskForm(task) {
	$("#updateTaskForm").show();
	$("#taskTitle").val(task.title);
}