<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <xsl:param name="admon.style">
      <xsl:text>margin-left: 0in; margin-right: 0in;</xsl:text>
    </xsl:param>

    <xsl:param name="id.warnings" select="0"/>

    <xsl:param name="html.stylesheet">../stylesheet.css</xsl:param>

    <xsl:param name="chunker.output.encoding" select="'utf-8'"/>
    <xsl:param name="chunk.section.depth" select="0" />
    <xsl:param name="use.id.as.filename" select="'1'" />
    <xsl:param name="chunk.quietly" select="1"/>

    <xsl:param name="callout.graphics.path">../images/callouts/</xsl:param>
    <xsl:param name="email.delimiters.enabled">0</xsl:param>
    <xsl:param name="draft.watermark.image" select="'../images/draft.png'"/>

    <!-- xml profiling option -->
    <xsl:param name="profile.condition" select="$target.product"/>

</xsl:stylesheet>
