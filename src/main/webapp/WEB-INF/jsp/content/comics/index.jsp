<%@ include file="/WEB-INF/jsp/taglibs/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<title>M-Dashboard</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
<link rel="stylesheet" href="../resources/css/bulma.css">
</head>
<body>
	<nav class="nav has-shadow"> 
		<tiles:insertAttribute name="header" />
	</nav>

	<section class="section">
	<div class="container">
		<h1 class="title">Titre</h1>
		<hr />
		<div class="columns">
			<div class="column is-3">
				<tiles:insertAttribute name="menu" />
			</div>
			<div class="column">
				<div class="content">Comics</div>
			</div>
		</div>
	</div>
	</section>

<script src="<c:url value="/resources/js/jquery.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/resources/js/comics/ajaxComics.js"/>" type="text/javascript"></script>
</body>
</html>