<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html>
<head>

<%@ include file="../../layer/scripts.jsp" %>

<meta charset="ISO-8859-1">
<title>Editar producto</title>


<script type="text/javascript">
	$(document).ready(function() {
		
		// Make sure it always shows root category at first
		$("#changeCategoryButton").click(function() {
			requestCategoryRoot();
		});
		
		$("#returnRootCategoryLink").click(function() {
			requestCategoryRoot();
		});
		
		$("#showSubCategoriesButton").click(function() {
			var data = $('#showCategoriesForm').serializeArray();
			// call requestCategoryChildren() function only if one category was selected
			if(data.find(element => element.name === 'parentCategoryId')) {
				requestCategoryChildren(data);
			}
		});
		
		$('#chooseCategoryButton').click(function() {
			var data = $('#showCategoriesForm').serializeArray();
			// Call updateCategoryRequest() function only if one category was selected
			if(data.find(element => element.name === 'parentCategoryId')) {
				updateCategoryRequest(data);
			}
			
		});
		
	});
	
	function requestCategoryChildren(categoryForm) {
		$('#returnRootCategoryLink').show();
		$.ajax({
			url: '/api/v1/categories/category-children',
			type: 'get',
			dataType: 'json',
			data: categoryForm,
			
			// Response in an array with the children categories of the selected category
			// Use showCategories function to show the children categories returned
			success: function(response) {
				showCategories(response);
			}
		});
	}
	
	function requestCategoryRoot() {
		$('#returnRootCategoryLink').hide();
		$.ajax({
			url: '/api/v1/categories/root-category',
			type: 'get',
			dataType: 'json',
			
			success: function(response) {
				// Response is an array containing only root category
				// Use showCategories function to show the root category returned
				showCategories(response);
				
			}
		});
	}
	
	function updateCategoryRequest(categoryForm) {
		$.ajax({
			url: '/api/v1/categories/update-category',
			type: 'post',
			data: categoryForm,
			
			success: function(response) {
				window.location.reload();
			}
		});
	}
	
	// Appends radio buttons to showCategoriesForm to show the children
	// categories of the selected category.
	function showCategories(categories) {
		if(categories.length !== 0) {
			// First dlete radio buttons that are currently in the form.
			$('#showCategoriesForm').children('div').remove();
			// Add one radio button for each category
			$.each(categories, function(index, category) {
				$('#showCategoriesForm').append(
						'<div class="form-check form-check-inline">' +
							'<input class="form-check-input" type="radio" name="parentCategoryId" id="category' + category.id + '" value="' + category.id + '">' +
							'<label class="form-check-label font-weight-bold" for="category' + category.id + '">' + category.name + '</label>' +
						'</div>'
				);
			});
		} else {
			// Show message: there are no subcategories for the selected category.
		}
	}
	
</script>

</head>
<body>


<%@ include file="../../layer/navbar.jsp" %>
	
	<div class="container">
	
		<%@ include file="admin-products-layer/admin-products-navbar.jsp" %>
	
		<div class="row mt-2">
			<div class="col-md-3 text-center">
				<%@ include file="../admin-layer/admin-sidebar.jsp" %>
			</div>
			<div class="col-md-9">
				
				<h3 class="h3 text-center mt-3">Editar Producto</h3>
				
				<div class="row mt-5">
					<div class="col-md-4">
						<img 
							class="img-thumbnail text-md-center" 
							src="data:image/jpeg;base64,${productImage }" 
							alt="Image was not found" />
						
						<form 
							id="updateProductPictureForm" 
							enctype="multipart/form-data" 
							method="post" 
							action="<c:url value="/admin/products/${product.id }/updateImage" />">
							<div class="custom-file mt-3">
								<input 
									type="file" 
									class="custom-file-input" 
									name="imageToUpdate"
									id="newProductPicture">
								<label id="newProductPictureLabel" class="custom-file-label" for="newProductPicture">Elige una foto</label>
							</div>
							<script type="application/javascript">
							    $('#newProductPicture').change(function(e){
							        var fileName = e.target.files[0].name;
							        $('#newProductPictureLabel').html(fileName);
							    });
							</script>
							<sec:csrfInput/>
							<div class="flex-box mt-3">
								<button type="submit" class="btn btn-primary">Actualizar</button>
							</div>
						</form>
					</div>
					<div class="col-md-8 pl-0">
						<nav aria-label="breadcrumb mb-0">
							<p class="mb-0 pb-0">Categorias:</p>
							<ol class="breadcrumb mb-0" style="background-color: white;">
								<c:forEach items="${productCategoriesPath }" var="category">
									<li class="breadcrumb-item active" aria-current="page">
										${category.name }
									</li>
								</c:forEach>
							</ol>
						</nav>
						<!-- Button trigger modal -->
						<div class="container d-flex">
							<button id="changeCategoryButton" type="button" class="btn btn-outline-primary btn-sm mt-0 mb-3 ml-auto" data-toggle="modal" data-target="#exampleModalCenter">
								Cambiar categoria
							</button>
						</div>
						<!-- Modal -->
						<div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
							<div class="modal-dialog modal-dialog-centered" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title" id="exampleModalLongTitle">Elige una categoria</h5>
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
									</div>
									<div class="modal-body">
										<div class="container" id="showCategoriesContainer">
											<div class="container d-flex" id="returnRootCategoryContainer">
												<a id="returnRootCategoryLink" href="#" class="badge badge-info ml-auto mb-3">Regresar a la categoría raíz</a>
											</div>
											<form class="form-inline" id="showCategoriesForm">
												<input type="hidden" name="productId" value="${product.id }" />
												<sec:csrfInput/>
											</form>
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
										<button type="button" class="btn btn-primary" id="showSubCategoriesButton">Ver subcategorias</button>
										<button type="button" class="btn btn-primary" id="chooseCategoryButton">Elegir</button>
									</div>
								</div>
							</div>
						</div>
						<form
							class="form mb-5 px-2" 
							method="post"
							action="<c:url value="/admin/products/${product.id }/updateProduct" />">
							<div class="row">
								<div class="col">
									<div class="row">
										<div class="col">
											<div class="form-group">
												<label for="name">Nombre:</label> 
												<input 
													class="form-control"
													type="text" 
													name="name" 
													id="name" 
													value="${product.name }">
											</div>
										</div>
									</div>
									
									<div class="row mt-3">
										<div class="col">
											<label for="price">Precio</label>
										</div>
										<div class="col">
											<div class="form-group">
												<input 
													class="form-control" 
													type="number"
													name="newPrice"
													id="price"
													value="${currentPrice.price }">
											</div>
										</div>
									</div>
									<div class="row mt-3">
										<div class="col">
											<div class="form-group">
												<label for="description">Descripción</label>
												<textarea 
													name="description" 
													class="form-control" 
													id="description" 
													rows="3">${product.description }</textarea>
											</div>
										</div>
									</div>
									<sec:csrfInput/>
									<div class="row">
										<div class="col d-flex justify-content-end">
											<button class="btn btn-primary mr-5 mt-5" type="submit">Guardar cambios</button>
										</div>
									</div>
								</div>
							</div> <!-- End of row -->
						</form>
					</div> <!-- End of col -->
				</div> <!-- End of row -->
			</div>
		</div>
	</div>

</body>
</html>