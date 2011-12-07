<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>If with Body</title>
  </head>

  <body>
    <c:if test="${pageContext.request.method=='POST'}">
      <c:if test="${param.guess=='Java'}">You guessed it!
      <br />

      <br />

      <br />
      </c:if>

      <c:if test="${param.guess!='Java'}">
		You are wrong
      <br />

      <br />

      <br />
      </c:if>
    </c:if>

    <form method="post">Guess what computer language
                        I am thinking of?
    <input type="text" name="guess" />

    <input type="submit" value="Try!" />

    <br />
    </form>
  </body>
</html>