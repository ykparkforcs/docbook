<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions"
                version='1.0'>

<!-- ================= or self::chapter=================================================== -->

  <xsl:template name="toc.line">
    <xsl:param name="toc-context" select="NOTANODE"/>

    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>

    <xsl:variable name="label">
      <xsl:apply-templates select="." mode="label.markup"/>
    </xsl:variable>

    <fo:block xsl:use-attribute-sets="toc.line.properties">
      <fo:inline keep-with-next.within-line="always">
        <fo:basic-link internal-destination="{$id}">

          <!-- chapter name -->
          <xsl:if test="self::chapter">
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="local-name()"/>
            </xsl:call-template>
            <xsl:copy-of select="$label"/>
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="'chapter-end'"/>
            </xsl:call-template>

            <xsl:value-of select="$autotoc.label.separator"/>
          </xsl:if>

          <!-- appendix name -->
          <xsl:if test="self::appendix">
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="local-name()"/>
            </xsl:call-template>
            <xsl:text>&#160;</xsl:text>
          </xsl:if>

          <!-- figure, table -->
          <xsl:if test="self::figure|self::table|self::example|self::equation">
            <xsl:text>&#91;</xsl:text>
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="local-name()"/>
            </xsl:call-template>
            <xsl:text>&#160;</xsl:text>
            <xsl:copy-of select="$label"/>
            <xsl:text>&#93;</xsl:text>
            <xsl:value-of select="$autotoc.label.separator"/>
          </xsl:if>

          <!-- else if -->
          <xsl:if test="$label != '' and not(self::chapter) and not (self::figure|self::table|self::example|self::equation)">
            <xsl:copy-of select="$label"/>
            <xsl:text>&#46;</xsl:text>
            <xsl:value-of select="$autotoc.label.separator"/>
          </xsl:if>

          <!-- title -->
          <xsl:apply-templates select="." mode="title.markup"/>
        </fo:basic-link>
      </fo:inline>

      <!-- page number -->
      <fo:inline keep-together.within-line="always">
        <xsl:text> </xsl:text>
        <fo:leader leader-pattern="dots"
                   leader-pattern-width="3pt"
                   leader-alignment="reference-area"
                   keep-with-next.within-line="always"/>
          <xsl:text> </xsl:text>
          <fo:basic-link internal-destination="{$id}">
            <fo:page-number-citation ref-id="{$id}"/>
          </fo:basic-link>
      </fo:inline>
    </fo:block>
  </xsl:template>

<!-- ==================================================================== -->

</xsl:stylesheet>
