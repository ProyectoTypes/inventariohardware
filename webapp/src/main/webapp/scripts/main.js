var isisURL, username, password, header;

$(document).ready(function(){ //For Normal usage
//document.addEventListener("deviceready", function(){ //For Phonegap
	toastr.options = {
		"debug": false,
		"positionClass": "toast-bottom-full-width",
		"onclick": null,
		"fadeIn": 300,
		"fadeOut": 1000,
		"timeOut": 3000,
		"extendedTimeOut": 1000
	};
    $('#loginButton').click(function(e,data){
    	//#username se conecta con el id de index.html, en la linea 47
		username = $('#username').val();
		//#password se conecta con el id de index.html, en la linea 53
		password = $('#password').val();
		isisURL = $('#url').val();
		////////////////////////
		//Esta parte hace la validación del login al entre el usuario y el sistema.
		////////////////////////
		//Las variables username y password son cargadas desde la linea 18 y 16 de main.js.
		////////////////////////
		if(username.length > 0 && password.length > 0 && isisURL.length > 0){
			if(isisURL[isisURL.length - 1] !== '/'){
				isisURL = isisURL + '/';
			}
			header = "Basic " + $.base64.encode(username + ":" + password);
			$.ajax({
				url: isisURL, //Localbox
				beforeSend: function(xhr) {
					xhr.setRequestHeader("Authorization", header);
					xhr.setRequestHeader("Accept", "application/json");
					$.mobile.showPageLoadingMsg(true);
				},
				complete: function() {
					$.mobile.hidePageLoadingMsg();
				},
				success: function (result) {
					console.log(result);
					$('#home').load('./Content/partials/homePage.html', function(){ // For Phonegap
							var homePageList = '#home #homePageList';
							$(homePageList).empty();
							for(i = 0; i < result.links.length ; i++){
								if(result.links[i].rel.indexOf("self") == -1 && result.links[i].rel.indexOf("domain-types") == -1){
									var homePageObjectHref = result.links[i].href;
									console.log(homePageObjectHref);
									var homePageObjectName = result.links[i].rel.split("/");
									console.log(homePageObjectName);
									homePageObjectName = homePageObjectName[1];
									$(homePageList).append('<li data-theme="c"><a class="homePageObject" data-href="'+homePageObjectHref+'" data-transition="slide">'+homePageObjectName+'</a></li>');
								}
							}
							$(homePageList).listview().listview('refresh');
							$(this).trigger("pagecreate");
					});
					$.mobile.changePage("#home");
				},
				error: function (request,error) {
					console.log(request.responseText);
					toastr.error('Usuario y contraseña incorrecto. Linea 56 de main.js');
				}
			});
		} else {
			toastr.error('Todos los campos son obligatorios. Linea 60 de main.js');
		}
    });
	
	$('a.homePageObject').livequery("click",function(){
		var resource_url = $(this).attr('data-href');
		$.ajax({
			url: resource_url,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (result) {
				console.log(result);
				if (resource_url.indexOf("user") != -1){
					$('#user').load('./Content/partials/user.html', function(){
						for (var userObject in result){
							if ( userObject.indexOf("userName") != -1){
								$('#userNameText').html(result[userObject]);
							} else if ( userObject.indexOf("friendlyName") != -1){
								$('#friendlyNameText').html(result[userObject]);
							} else if ( userObject.indexOf("email") != -1){
								$('#emailText').html(result[userObject]);
							} else if ( userObject.indexOf("roles") != -1){
								if (result[userObject].length > 0) {
									$('#rolesText').html(result[userObject]);
								}
							}
						}
						$(this).trigger("pagecreate");
					});
					$.mobile.changePage("#user");
				} else if (resource_url.indexOf("version") != -1){
					$('#version').load('./Content/partials/version.html', function(){
						for (var userObject in result) {
							if ( userObject.indexOf("specVersion") != -1){
								$('#specVersionText').html(result[userObject]);
							} else if ( userObject.indexOf("implVersion") != -1){
								$('#implVersionText').html(result[userObject]);
							}
						}
						$(this).trigger("pagecreate");
					});
					$.mobile.changePage("#version");
				} else if (resource_url.indexOf("services") != -1){
					$('#services').load('./Content/partials/services.html', function(){
						var servicesList = '#services #servicesList';
						$(servicesList).empty();
						for(i = 0; i < result.value.length ; i++){
							$(servicesList).append('<li data-theme="c"><a class="service" data-href="'+result.value[i].href+'" data-transition="slide">'+result.value[i].title+'</a></li>');
						}
						$(servicesList).listview().listview('refresh');
						$(this).trigger("pagecreate");
					});
					$.mobile.changePage("#services");
				}
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("Todos los campos son obligatorios. Linea 123 de main.js");
			}
		});
    });
	
	$('a.service').livequery("click",function(){
		$.ajax({
			url: $(this).attr('data-href'),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (result) {
				var collections = result.members;
				console.log(collections);
				$('#service').load('./Content/partials/service.html', function(){
					var collectionsList = '#service #collectionsList';
					$(collectionsList).empty();
					for(var collection in collections){
						$(collectionsList).append('<li data-theme="c"><a class="collection" data-href="'+collections[collection].links[0].href+'" data-transition="slide">'+collection+'</a></li>');
					}
					$(collectionsList).listview().listview('refresh');
					$(this).trigger("pagecreate");
				});
				$.mobile.changePage("#service");
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("No se pudo obtener colecciones. Linea 155 de main.js");
			}
		});
    });
	
	$('a.collection').livequery("click",function(){
		var collection_url = $(this).attr('data-href');
		$.ajax({
			url: collection_url,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				var links = data.links;
				console.log(links);
				var invoke_url = links[2].href;
				var invoke_method = links[2].method;
				var parameters = data.parameters;
				if(invoke_method == "GET"){
					if(parameters.length == 0){
						$.ajax({
							type: invoke_method,
							url: invoke_url,
							beforeSend: function(xhr) {
								xhr.setRequestHeader("Authorization", header);
								xhr.setRequestHeader("Accept", "application/json");
								$.mobile.showPageLoadingMsg(true);
							},
							complete: function() {
								$.mobile.hidePageLoadingMsg();
							},
							success: function (data) {
								var resultType = data.resulttype;
								var objects = data.result.value;
								console.log(objects);
								if(resultType == "list"){
									$('#objects').load('./Content/partials/objects.html', function(){
										var objectsList = '#objects #objectsList';
										$(objectsList).empty();
										for(i = 0; i < objects.length; i++){
											$(objectsList).append('<li data-theme="c"><a class="object" data-href="'+objects[i].href+'" data-transition="slide">'+objects[i].title+'</a></li>');
										}
										$(objectsList).listview().listview('refresh');
										$(this).trigger("pagecreate");
									});
									$.mobile.changePage("#objects");
								} else if(resultType == "scalarvalue") {
									toastr.success(objects);
								}
							},
							error: function (request,error) {
								console.log(request.responseText);
								toastr.error("No se puede invocar la acción. Linea 211 de main.js");
							}
						});
					}
				} else if(invoke_method == "POST"){
					var id = data.id;
					if(parameters.length <= 0){
						$.ajax({
							type: invoke_method,
							url: invoke_url,
							beforeSend: function(xhr) {
								xhr.setRequestHeader("Authorization", header);
								xhr.setRequestHeader("Accept", "application/json");
								$.mobile.showPageLoadingMsg(true);
							},
							complete: function() {
								$.mobile.hidePageLoadingMsg();
							},
							success: function (data) {
								handleResult(data);
							},
							error: function (request,error) {
								console.log(request.responseText);
								toastr.error("No se puede invocar la acción. Linea 235 de main.js");
							}
						});
					} else {
						$('#parameters').load('./Content/partials/parameters.html', function(){
							var parameterContent = '#parameters div.content';
							$(parameterContent).empty();
							console.log(parameters);
							$(parameterContent).append(htmlForParameters(parameters,invoke_url,invoke_method));
							$(this).trigger("pagecreate");
						});
						$.mobile.changePage("#parameters");
					}
				}
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("No se puede invocar la acción. Linea 252 de main.js");
			}
		});
    });
	
	$('a.parameterSubmit').livequery("click",function(){
		var newDetails = JSON.stringify($(this).parent().children('form').serializeObject());
		console.log(newDetails);
		var updateUrl = $(this).attr('data-href');
		var invokeMethod = $(this).attr('data-method');
		var toastMsg = "Acción completado con éxito";
		$.ajax({
			type: invokeMethod,
			url: updateUrl,
			data: newDetails,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				//toastr.success(toastMsg);
				handleResult(data);
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("Se requiere que esten completas las variables. Linea 281 de main.js");
			}
		});
	});
	
	$('a.object').livequery("click",function(){
		$.ajax({
			url: $(this).attr('data-href'),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				var objectDetails = data.members;
				var put_url = data.links[2].href;
				console.log(objectDetails);
				$("#object").load('./Content/partials/object.html', function(){
					updateObjectPage(objectDetails, put_url);
					$(this).trigger("pagecreate");
				});
				$.mobile.changePage("#object");
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("No se pudo obtener las propiedades del objeto. Linea 309 de main.js");
			}
		});
    });
	
	$('a.objectCollection').livequery("click",function(){
		var disabled = $(this).attr('data-disabled');
		if(disabled != 0){
			toastr.info(disabled);
		} else{
			$('#similarObjects').load('./Content/partials/objects.html', function(){
				$(this).trigger("pagecreate");
			});
			$.ajax({
				url: $(this).attr('data-href'),
				beforeSend: function(xhr) {
					xhr.setRequestHeader("Authorization", header);
					xhr.setRequestHeader("Accept", "application/json");
					$.mobile.showPageLoadingMsg(true);
				},
				complete: function() {
					$.mobile.hidePageLoadingMsg();
				},
				success: function (data) {
					var objects = data.value;
					console.log(objects);
					$.mobile.changePage("#similarObjects");
					$('#similarObjects #objectsList').empty();
					for(i = 0; i < objects.length; i++){
						$('#similarObjects #objectsList').append('<li data-theme="c"><a class="object" data-href="'+objects[i].href+'" data-transition="slide">'+objects[i].title+'</a></li>');
					}
					$('#similarObjects #objectsList').listview('refresh');
				},
				error: function (request,error) {
					console.log(request.responseText);
					toastr.error("No se pudo obtener colecciones. Linea 344 de main.js");
				}
			});
		}
    });
	
	$('a.objectAction').livequery("click",function(){
		var disabled = $(this).attr('data-disabled');
		if(disabled != 0){
			toastr.warning(disabled);
		} else{
			$.ajax({
				url: $(this).attr('data-href'),
				beforeSend: function(xhr) {
					xhr.setRequestHeader("Authorization", header);
					xhr.setRequestHeader("Accept", "application/json");
					$.mobile.showPageLoadingMsg(true);
				},
				complete: function() {
					$.mobile.hidePageLoadingMsg();
				},
				success: function (data) {
					var links = data.links;
					console.log(links);
					var invoke_url = links[2].href;
					var invoke_method = links[2].method;
					if(invoke_method == "GET"){
						$.ajax({
							type: invoke_method,
							url: invoke_url,
							beforeSend: function(xhr) {
								xhr.setRequestHeader("Authorization", header);
								xhr.setRequestHeader("Accept", "application/json");
								$.mobile.showPageLoadingMsg(true);
							},
							complete: function() {
								$.mobile.hidePageLoadingMsg();
							},
							success: function (data) {
								var resultType = data.resulttype;
								var objects = data.result.value;
								if(resultType == "list"){
									$('#similarToObjects').load('./Content/partials/similarObjects.html', function(){
										$('#similarToObjects #objectsSimilarList').empty();
										console.log(objects);
										for(i = 0; i < objects.length; i++){
											console.log("Children length: "+$('#similarToObjects #objectsSimilarList').children().length);
											$('#similarToObjects #objectsSimilarList').append('<li data-theme="c"><a class="object" data-href="'+objects[i].href+'" data-transition="slide">'+objects[i].title+'</a></li>');
											console.log("Children length: "+$('#similarToObjects #objectsSimilarList').children().length);
										}
										console.log("Children length: "+$('#similarToObjects #objectsSimilarList').children().length);
										$('#similarToObjects #objectsSimilarList').listview().listview('refresh');
										$(this).trigger("pagecreate");
									});
									$.mobile.changePage("#similarToObjects");
								}
							},
							error: function (request,error) {
								console.log(request.responseText);
								toastr.error("No se puede invocar la acción. Linea 403 de main.js");
							}
						});
					} else if(invoke_method == "POST"){
						var params = data.parameters;
						var id = data.id;
						if(params.length <= 0){
							$.ajax({
								type: invoke_method,
								url: invoke_url,
								beforeSend: function(xhr) {
									xhr.setRequestHeader("Authorization", header);
									xhr.setRequestHeader("Accept", "application/json");
									$.mobile.showPageLoadingMsg(true);
								},
								complete: function() {
									$.mobile.hidePageLoadingMsg();
								},
								success: function (data) {
									var resultType = data.resulttype;
									var objects = data.result.value;
									if(resultType == "list"){
										$.mobile.changePage("#objects");
										$('#objects #objectsList').empty();
										console.log(objects);
										for(i = 0; i < objects.length; i++){
											$('#objects #objectsList').append('<li data-theme="c"><a class="object" data-href="'+objects[i].href+'" data-transition="slide">'+objects[i].title+'</a></li>');
										}
										$('#objects #objectsList').listview('refresh');
									} else if(resultType == "domainobject"){
										updateObjectPage(data.result.members, data.result.links[2].href);
										$.mobile.changePage("#object");
									}
								},
								error: function (request,error) {
									console.log(request.responseText);
									toastr.error("No se puede invocar la acción. Linea 439 de main.js");
								}
							});
						} else if(params.length == 4){
							$('#parameters').load('./Content/partials/parameters.html', function(){
								var parameterContent = '#parameters div.content';
								$(parameterContent).empty();
								console.log(params);
								$(parameterContent).append(htmlForParameters(params,invoke_url,invoke_method));
								$(this).trigger("pagecreate");
							});
							$.mobile.changePage("#parameters");
						} else if(params.length == 1){
							if(id === "add"){
								$('#addDependency').load('./Content/partials/addDependency.html', function(){
									$.ajax({
										url: isisURL+"services/toDoItems/actions/allToDos/invoke",
										beforeSend: function(xhr) {
											xhr.setRequestHeader("Authorization", header);
											xhr.setRequestHeader("Accept", "application/json");
											$.mobile.showPageLoadingMsg(true);
										},
										complete: function() {
											$.mobile.hidePageLoadingMsg();
										},
										success: function (data) {
											var objects = data.result.value;
											console.log(objects);
											for(i = 0; i < objects.length; i++){
												if(invoke_url.indexOf(objects[i].href) == -1){
													$("#addDependency #addDependencyMenu").append($('<option />',{'value': objects[i].href}).text(objects[i].title));
												} else {
													$("#addDependency #addDependencyMenu").append($('<option />',{'value': objects[i].href, 'disabled': 'disabled'}).text(objects[i].title));
												}
											}
											$("#addDependency #addDependencyMenu").selectmenu();
											$("#addDependency #addDependencyMenu").selectmenu( "refresh", true );
											$("a.addDependency").attr("data-href", invoke_url);
											$.mobile.changePage("#addDependency");
										},
										error: function (request,error) {
											console.log(request.responseText);
											toastr.error("No se puede invocar la acción. Linea 481 de main.js");
										}
									});
									$(this).trigger("pagecreate");
								});
							} else if(id === "remove"){
								$('#removeDependency').load('./Content/partials/removeDependency.html', function(){
									var objects = params[0].choices;
									console.log(objects);
									$.mobile.changePage("#removeDependency");
									for(i = 0; i < objects.length; i++){
										console.log("In loop"+objects);
										$("#removeDependency #removeDependencyMenu").append($('<option />',{'value': objects[i].href}).text(objects[i].title));
									}
									$("#removeDependency #removeDependencyMenu").selectmenu();
									$("#removeDependency #removeDependencyMenu").selectmenu( "refresh", true );
									$("a.removeDependency").attr("data-href", invoke_url);
									$(this).trigger("pagecreate");
								});
							} else {
								$('#parameters').load('./Content/partials/parameters.html', function(){
									var parameterContent = '#parameters div.content';
									$(parameterContent).empty();
									console.log(params);
									$(parameterContent).append(htmlForParameters(params,invoke_url,invoke_method));
									$(this).trigger("pagecreate");
								});
								$.mobile.changePage("#parameters");
							}
						}
					}
				},
				error: function (request,error) {
					console.log(request.responseText);
					toastr.error("No se puede invocar la acción. Linea 515 de main.js");
				}
			});
		}
    });
	
	$("a.addDependency").livequery("click",function(){
		var objectHref = $("#addDependency #addDependencyMenu").val();
		var dependencyObject = {"toDoItem":{"value": { "href": objectHref } }} ;
		dependencyObject = JSON.stringify(dependencyObject);
		console.log(dependencyObject);
		$.ajax({
			type: "POST",
			url: $(this).attr('data-href'),
			data: dependencyObject,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				window.history.back();
				updateObjectPage(data.result.members, data.result.links[2].href);
				toastr.success('Dependencia del objeto agregado con éxito. Linea 541 de main.js');
			},
			error: function (request,error) {
				var response = eval('(' + request.responseText + ')');
				console.log(response["x-ro-invalidReason"]);
				toastr.error("No se pudo agregar la dependencia. Linea 546 de main.js "+response["x-ro-invalidReason"]);
			}
		});
	});
	
	$("a.removeDependency").livequery("click",function(){
		var objectHref = $("#removeDependency #removeDependencyMenu").val();
		var dependencyObject = {"toDoItem":{"value": { "href": objectHref } }} ;
		dependencyObject = JSON.stringify(dependencyObject);
		console.log(dependencyObject);
		$.ajax({
			type: "POST",
			url: $(this).attr('data-href'),
			data: dependencyObject,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				window.history.back();
				updateObjectPage(data.result.members, data.result.links[2].href);
				toastr.success('Dependencia del objeto agregado con éxito. Linea 571 main.js');
			},
			error: function (request,error) {
				/* console.log(error);
				toastr.error('Username and Password donot match!'); */
				var response = eval('(' + request.responseText + ')');
				console.log(response["x-ro-invalidReason"]);
				toastr.error("Dependencia no puede ser eliminado. Linea 578 main.js "+response["x-ro-invalidReason"]);
			}
		});
	});
	
	$('.createObject, .updateObject').click(function(){
		var newDetails = JSON.stringify($(this).parent().children('form').serializeObject());
		console.log(newDetails);
		var updateUrl = $(this).attr('data-href');
		var toastMsg = "Objeto actualizado con éxito. Linea 587 main.js";
		if(updateUrl.indexOf("duplicate") != -1){
			toastMsg = "Objeto duplicado con éxito. Linea 589 main.js";
		}
		$.ajax({
			type: "POST",
			url: updateUrl,
			data: newDetails,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				toastr.success(toastMsg);
				$.mobile.changePage("#service");
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("El objeto no se pudo actualizar. Lina 609 de main.js");
			}
		});
	});
	
	$('#editObject').livequery('click',function(){
		var newDetails = JSON.stringify($(this).parent().children('form').serializeObject());
		console.log(newDetails);
		var put_url = $(this).attr('data-href');
		$.ajax({
			type: "PUT",
			url: put_url,
			data: newDetails,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				updateObjectPage(data.members, put_url);
				toastr.success('Objeto actualizado correctamente. Linea 632 de main.js');
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("El objeto no se pudo actualizar. Linea 636 de main.js");
			}
		});
	});
	
	
	
	$('#createObject').click(function(){
		var newDetails = JSON.stringify($('.newObjectDetails form').serializeObject());
		console.log(newDetails);
		$.ajax({
			type: "POST",
			url: $(this).attr('data-href'),
			data: newDetails,
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Authorization", header);
				xhr.setRequestHeader("Accept", "application/json");
				$.mobile.showPageLoadingMsg(true);
			},
			complete: function() {
				$.mobile.hidePageLoadingMsg();
			},
			success: function (data) {
				toastr.success('Objeto creado con éxito. Linea 659 main.js');
				$.mobile.changePage("#service");
			},
			error: function (request,error) {
				console.log(request.responseText);
				toastr.error("Objeto no se pudo crear. Linea 664 main.js");
			}
		});
	});
});
