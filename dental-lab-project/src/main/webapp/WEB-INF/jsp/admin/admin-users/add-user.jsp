<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

<html>
<head>
<%@ include file="../../layer/scripts.jsp"%>

<meta charset="ISO-8859-1">
<title>Crear nuevo usuario</title>
</head>
<body>
	<%@ include file="../../layer/navbar.jsp"%>

	<div class="container">

		<%@ include file="admin-users-layer/admin-users-navbar.jsp"%>

		<div class="row mt-2">
			<div class="col-md-3 text-center">
				<%@ include file="../admin-layer/admin-sidebar.jsp"%>
			</div>
			<div class="col-md-9">
				<div class="container">

					<h3 class="h3 text-center mt-3">Agregar un nuevo usuario</h3>

					<form class="form mt-5"
							action="<c:url value="/admin/users/add" />" method="post">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="firstName">Nombre</label> <input
										class="form-control" type="text" name="firstName"
										id="firstName">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="firstLastName">Apellido paterno</label> <input
										class="form-control" id="firstLastName" type="text"
										name="firstLastName">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="secondLastName">Apellido materno</label> <input
										class="form-control" id="secondLastName" type="text"
										name="secondLastName">
								</div>
							</div>
						</div>
						<div class="row mt-3">
							<div class="col-md-6">
								<div class="form-group">
									<label for="username">Nombre de usuario</label> <input
										class="form-control" type="text" name="username" id="username">
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label for="email">Correo electronico</label> <input
										class="form-control" type="email" name="email" id="email">
								</div>
							</div>
						</div>
						<div class="row mt-3">
							<div class="col-md-6">
								<div class="form-group">
									<label for="password">Contraseña</label> <input
										class="form-control" type="password" name="password"
										id="password">
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label for="confirmPassword">Confirmar contraceña</label> <input
										class="form-control" type="password" name="confirmPassword"
										id="confirmPassword">
								</div>
							</div>
						</div>
						<sec:csrfInput />
						<div class="row">
							<div class="col d-flex justify-content-end">
								<button class="btn btn-primary mr-1 mt-5" type="submit">Crear Usuario</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>

	</div>

</body>
</html>