<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>

	<xsl:template match="/">
		<html>
			<head>
				<title>Strangebrew Recipe</title>
			</head>
			<body>
				<h1>Strangebrew Recipe</h1>
				<xsl:apply-templates select="STRANGEBREWRECIPE/DETAILS"/>
				<xsl:apply-templates select="STRANGEBREWRECIPE/FERMENTABLES"/>
				<xsl:apply-templates select="STRANGEBREWRECIPE/HOPS"/>
				<xsl:apply-templates select="STRANGEBREWRECIPE/MASH"/>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="STRANGEBREWRECIPE/DETAILS">
		<table>
			<thead>
				<tr>
					<th colspan="4">Details</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>Name</th>
					<td><xsl:value-of select="NAME"/></td>
					<th><abbr title="Original Gravity">OG</abbr></th>
					<td><xsl:value-of select="OG"/></td>
				</tr>
				<tr>
					<th>Brewer</th>
					<td><xsl:value-of select="BREWER"/></td>
					<th><abbr title="Final Gravity">FG</abbr></th>
					<td><xsl:value-of select="FG"/></td>
				</tr>
				<tr>
					<th>Style</th>
					<td><xsl:value-of select="STYLE"/></td>
					<th><abbr title="International Bittering Unit">IBU</abbr></th>
					<td><xsl:value-of select="IBU"/></td>
				</tr>
				<tr>
					<th>Date</th>
					<td><xsl:value-of select="RECIPE_DATE"/></td>
					<th><abbr title="Standard Reference Model (color scale)">SRM</abbr></th>
					<td><xsl:value-of select="LOV"/></td>
				</tr>
				<tr>
					<th>Volume</th>
					<td><xsl:value-of select="SIZE"/> <xsl:value-of select="SIZE_UNITS"/></td>
					<th><abbr title="Alcohol By Volume">ABV</abbr></th>
					<td><xsl:value-of select="ALC"/>%</td>
				</tr>
				<tr>
					<th>Boil Time</th>
					<td><xsl:value-of select="BOIL_TIME"/> minutes</td>
					<th>Efficiency</th>
					<td><xsl:value-of select="EFFICIENCY"/>%</td>
				</tr>
				<tr>
					<th>Yeast</th>
					<td><xsl:value-of select="YEAST"/></td>
					<th>Attenuation</th>
					<td><xsl:value-of select="ATTENUATION"/>%</td>
				</tr>
				<xsl:apply-templates select="NOTES"/>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="NOTES">
		<tr>
			<th>Comments</th>
			<td colspan="3"><xsl:value-of select="."/></td>
		</tr>
	</xsl:template>

	<xsl:template match="STRANGEBREWRECIPE/FERMENTABLES">
		<table>
			<thead>
				<tr>
					<th colspan="5">Fermentables</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>Name</th>
					<th>Points</th>
					<th>Quantity</th>
					<th>Units</th>
					<th>Cost</th>
				</tr>
				<xsl:for-each select="ITEM">
					<tr>
						<td><xsl:value-of select="MALT"/></td>
						<td><xsl:value-of select="POINTS"/></td>
						<td><xsl:value-of select="AMOUNT"/></td>
						<td><xsl:value-of select="UNITS"/></td>
						<td><xsl:value-of select="COSTLB"/></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="STRANGEBREWRECIPE/HOPS">
		<table>
			<thead>
				<tr>
					<th colspan="7">Hops</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>Name</th>
					<th>Alpha</th>
					<th>Form</th>
					<th>Quantity</th>
					<th>Units</th>
					<th>Time</th>
					<th>Cost</th>
				</tr>
				<xsl:for-each select="ITEM">
					<tr>
						<td><xsl:value-of select="HOP"/></td>
						<td><xsl:value-of select="ALPHA"/></td>
						<td><xsl:value-of select="FORM"/></td>
						<td><xsl:value-of select="AMOUNT"/></td>
						<td><xsl:value-of select="UNITS"/></td>
						<td><xsl:value-of select="TIME"/></td>
						<td><xsl:value-of select="COSTOZ"/></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="STRANGEBREWRECIPE/MASH">
		<table>
			<thead>
				<tr>
					<th colspan="6">Mash Schedule</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>Type</th>
					<th>Start Temp</th>
					<th>End Temp</th>
					<th>Method</th>
					<th>Step Minutes</th>
					<th>Instructions</th>
				</tr>
				<xsl:for-each select="ITEM">
					<tr>
						<td><xsl:value-of select="TYPE"/></td>
						<td>
							<xsl:value-of select="DISPL_TEMP"/>
							<xsl:value-of select="/STRANGEBREWRECIPE/DETAILS/MASH_TMP_U"/>
						</td>
						<td>
							<xsl:value-of select="DISPL_END_TEMP"/>
							<xsl:value-of select="/STRANGEBREWRECIPE/DETAILS/MASH_TMP_U"/>
						</td>
						<td><xsl:value-of select="METHOD"/></td>
						<td><xsl:value-of select="MIN"/></td>
						<td><xsl:value-of select="DIRECTIONS"/></td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

</xsl:stylesheet>
