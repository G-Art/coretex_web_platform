<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/template/nav" %>

<ul class="sidebar navbar-nav">

    <nav:linkElement text="Types" path="/types" img_class="fa-database"/>
    <nav:linkElement text="Query" path="/types/query" img_class="fa-question"/>
    <nav:linkElement text="Items" path="/items" img_class="fa-receipt"/>

</ul>
