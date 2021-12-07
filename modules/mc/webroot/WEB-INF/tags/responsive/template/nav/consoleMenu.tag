<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/template/nav" %>

<ul class="sidebar navbar-nav">

    <nav:linkElement text="System" path="/system" img_class="fa-layer-group"/>
    <nav:linkElement text="Logging" path="/" img_class="fa-book-medical"/>
    <nav:linkElement text="Query" path="/types/query" img_class="fa-question"/>
    <nav:linkElement text="Items" path="/items" img_class="fa-receipt"/>

</ul>
