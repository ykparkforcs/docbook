<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <xsl:param name="index.method">kimber</xsl:param>
    <xsl:param name="local.l10n.xml" select="document('gentext.xml')"/>

    <xsl:param name="glossterm.auto.link" select="'1'"/>

    <xsl:param name="targets.filename" select="'target.db'"/>
    <xsl:param name="target.database.document" select="'../olinkdb.xml'"/>
    <xsl:param name="olink.doctitle" select="'yes'"/>

    <xsl:param name="toc.section.depth">2</xsl:param>
    <xsl:param name="section.autolabel" select="1"/>
    <xsl:param name="section.label.includes.component.label" select="1"/>

    <xsl:param name="use.extensions">1</xsl:param>

</xsl:stylesheet>
