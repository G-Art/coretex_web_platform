
<%@ attribute name="active" required="true" %>

<label class="label ${active ? 'bg-success' : 'bg-danger'}">
    ${active ? 'Active' : 'Not Active'}
</label>