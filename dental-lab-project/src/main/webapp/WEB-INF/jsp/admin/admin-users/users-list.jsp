<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html>
<head>
<%@ include file="../../layer/scripts.jsp" %>

<meta charset="ISO-8859-1">
<title>Lista de usuarios</title>
<style type="text/css">
	
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#selectPageSize").change(function() {
			$("#usersListOptionsForm").submit();
		});
	});
	$(document).ready(function() {
		$("#selectSortBy").change(function() {
			$("#usersListOptionsForm").submit();
		});
	});
</script>
</head>
<body>

	<%@ include file="../../layer/navbar.jsp" %>
	
	<div class="container" id="body-container">
	
	<%@ include file="admin-users-layer/admin-users-navbar.jsp" %>
	
		<div class="row mt-2">
			<div class="col-md-3 text-center">
				<%@ include file="../admin-layer/admin-sidebar.jsp" %>
			</div>
			<div class="col-md-9">

				<div class="container pt-3" id="user-list-container">
				
					<h3 class="h3 mt-3 text-primary text-center pb-3">Lista de usuarios</h3>
					
					<div class="d-flex mt-4">
						<form id="usersListOptionsForm" class="form-inline ml-auto" action="<c:url value="/admin/users/list-options" />" method="post">
							<p class="my-auto pr-2">Usuarios a mostrar:</p>
							<select id="selectPageSize" name="pageSize" class="custom-select custom-select-sm">
								<c:forEach begin="1" end="30" var="i">
									<c:if test="${i != pageSize }">
										<option value="${i }">${i }</option>
									</c:if>
									<c:if test="${i == pageSize }">
										<option value="${pageSize }" selected>${pageSize }</option>
									</c:if>
								</c:forEach>
							</select>
							<p class="ml-2 my-auto pr-2">Ordenar por:</p>
							<select id="selectSortBy" name="sortBy" class="custom-select custom-select-sm">
								<option value="username" ${sortBy eq "username" ? "selected": "" }>Nombre de usuario</option>
								<option value="email" ${sortBy eq "email" ? "selected": "" }>Correo electronico</option>
								<option value="firstLastName" ${sortBy eq "firstLastName" ? "selected": "" }>Apellido paterno</option>
							</select>
							<sec:csrfInput/>
						</form>
					</div>
					
					<table class="table mt-3">
						<thead>
							<tr>
								<th scope="col">Nombre de usuario</th>
								<th scope="col">Correo electronico</th>
								<th scope="col">Nombre</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${usersPage.content }" var="user">
								<tr>
									<th scope="row">${user.username }</th>
									<td>${user.email }</td>
									<td>${user.firstLastName } ${user.secondLastName } ${user.firstName }</td>
									<td><a href="<c:url value="/admin/users/edit?user_id=${user.id }" />" class="btn btn-secondary">Editar</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<%@ include file="./admin-users-layer/users-list-navigation.jsp" %>
				</div>
			</div>
		</div>
	</div>

</body>
</html>