<%@include file="../_header.tag"%>

<ctrl:editField path="suburb" />
<ctrl:editField path="city" />
<ctrl:editField path="state" />
<ctrl:editField path="postcode" />
<ctrl:editNgSelect path="country" optionsUrl="/json/countries" optionsExpression="country.name for country in items track by country.name"  />
