<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- currentPage = usersPage.number and numberOfPages = usersPage.totalPages -->

<!-- Navigation -->
<c:set value="9" var="navigationSize" />
<nav aria-label="Page navigation">
	<ul class="pagination">
		<li class="page-item">
      		<a class="page-link" href="<c:url value="/admin/users/list?page_number=${usersPage.number > 0 ? usersPage.number - 1 : 0 }&page_size=${pageSize }&sort_by=${sortBy }" />" aria-label="Previous">
       			<span aria-hidden="true">&laquo;</span>
        		<span class="sr-only">Previous</span>
      		</a>
    	</li>
		<c:if test="${usersPage.number-navigationSize > 0 }">
			<li class="page-item">
				<a class="page-link" href="<c:url value="/admin/users/list?page_number=0&page_size=${pageSize }&sort_by=${sortBy }" />">0</a>
			</li>
			<li class="page-item">
				<a class="page-link" href="#" >...</a>
			</li>
		</c:if>
		<c:forEach begin="${usersPage.number - navigationSize >= 0 ? usersPage.number - navigationSize : 0 }" end="${usersPage.number + navigationSize < usersPage.totalPages ? usersPage.number + navigationSize : usersPage.totalPages - 1 }" var="i">
			<li class="page-item ${usersPage.number == i ? 'active' : '' }">
				<a class="page-link" href="<c:url value="/admin/users/list?page_number=${i }&page_size=${pageSize }&sort_by=${sortBy }" />">${i }</a>
			</li>
		</c:forEach>
		<c:if test="${usersPage.number+navigationSize < usersPage.totalPages-1 }">
			<li class="page-item">
				<a class="page-link" href="#" >...</a>
			</li>
			<li class="page-item">
				<a class="page-link" href="<c:url value="/admin/users/list?page_number=${usersPage.totalPages-1 }&page_size=${pageSize }&sort_by=${sortBy }" />">${usersPage.totalPages-1 }</a>
			</li>
		</c:if>
		<li class="page-item">
      		<a class="page-link" href="<c:url value="/admin/users/list?page_number=${usersPage.number < usersPage.totalPages-1 ? usersPage.number + 1 : usersPage.totalPages-1}&page_size=${pageSize }&sort_by=${sortBy }" />" aria-label="Next">
		        <span aria-hidden="true">&raquo;</span>
		        <span class="sr-only">Next</span>
      		</a>
    	</li>
	</ul>
</nav>