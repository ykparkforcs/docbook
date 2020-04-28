<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'>

<!-- ********************************************************************
     $Id: autotoc.xsl 6462 2007-01-04 09:55:38Z bobstayton $
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<!-- ================= or self::chapter=================================================== -->

    <!--
    <xsl:param name="toc-context" select="NOTANODE"/>

    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>

    <xsl:variable name="label">
      <xsl:apply-templates select="." mode="label.markup"/>
    </xsl:variable>
    -->

    <!-- chapter toc -->
    <!--
		<xsl:template match="chapter" mode="toc">
		  <xsl:param name="toc-context" select="."/>
		  <xsl:element name="{$toc.listitem.type}">
		  <a>
			  <xsl:attribute name="href">
	        <xsl:call-template name="href.target"/>
	      </xsl:attribute>
			  <xsl:call-template name="gentext">
			    <xsl:with-param name="key" select="local-name()"/>
			  </xsl:call-template>
			  <xsl:variable name="label">
		      <xsl:apply-templates select="." mode="label.markup"/>
		    </xsl:variable>
			  <xsl:copy-of select="$label"/>
			  <xsl:text>&#51109;</xsl:text>
			  <xsl:text>&#160;</xsl:text>
			  <xsl:apply-templates select="." mode="title.markup"/>
			</a>
		 </xsl:element>
		 <xsl:call-template name="subtoc">
				  <xsl:with-param name="toc-context" select="$toc-context"/>
		      <xsl:with-param name="nodes" select="section|sect1
		                                         |simplesect[$simplesect.in.toc != 0]
		                                         |refentry
		                                         |glossary|bibliography|index
		                                         |bridgehead[$bridgehead.in.toc != 0]"/>
		 </xsl:call-template>
		</xsl:template>
    -->

		<!-- appendix toc -->
		<!--
		<xsl:template match="appendix" mode="toc">
				<xsl:param name="toc-context" select="."/>
				<xsl:element name="{$toc.listitem.type}">
				  <a>
					  <xsl:attribute name="href">
			        <xsl:call-template name="href.target"/>
			      </xsl:attribute>
						<xsl:call-template name="gentext">
						<xsl:with-param name="key" select="local-name()"/>
						</xsl:call-template>
						<xsl:variable name="label">
						<xsl:apply-templates select="." mode="label.markup"/>
						</xsl:variable>
						<xsl:copy-of select="$label"/>
						<xsl:text>&#160;</xsl:text>
						<xsl:apply-templates select="." mode="title.markup"/>
						<xsl:call-template name="subtoc">
				      <xsl:with-param name="toc-context" select="$toc-context"/>
		          <xsl:with-param name="nodes" select="section|sect1
		                                         |simplesect[$simplesect.in.toc != 0]
		                                         |refentry
		                                         |glossary|bibliography|index
		                                         |bridgehead[$bridgehead.in.toc != 0]"/>
		        </xsl:call-template>
					</a>
		  </xsl:element>
		</xsl:template>
		-->

		<!-- figure, table toc -->
		<xsl:template match="figure|table|example|equation|procedure" mode="toc">
		  <xsl:param name="toc-context" select="."/>
		  <xsl:element name="{$toc.listitem.type}">
				<xsl:text>&#91;</xsl:text>
		    <xsl:call-template name="gentext">
		      <xsl:with-param name="key" select="local-name()"/>
		    </xsl:call-template>
		    <xsl:text>&#160;</xsl:text>
		    <xsl:variable name="label">
		      <xsl:apply-templates select="." mode="label.markup"/>
		    </xsl:variable>
		    <xsl:copy-of select="$label"/>
		    <xsl:text>&#93;</xsl:text>
		    <xsl:text>&#160;</xsl:text>
		    <!--
		    <xsl:if test="$label != ''">
		      <xsl:value-of select="$autotoc.label.separator"/>
		    </xsl:if>
		    -->
		    <a>
		      <xsl:attribute name="href">
		        <xsl:call-template name="href.target"/>
		      </xsl:attribute>
		      <xsl:apply-templates select="." mode="titleabbrev.markup"/>
		    </a>
		  </xsl:element>
		</xsl:template>

<!-- ==================================================================== -->

</xsl:stylesheet>