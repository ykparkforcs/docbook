<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:param name="logo.image" select="''"/>

    <xsl:param name="title.font.size" select="36"/>
    <xsl:param name="subtitle.font.size" select="36"/>
    <xsl:param name="titleabbrev.font.size" select="16"/>
    <xsl:param name="acknowledgements.font.size" select="12"/>
    <!--
     Parameters
        * words: 문자열
     Return
        * words가 null이면 1
        * 그렇지 않으면 words에 포함된 단어 개수 (' '를 기준으로 단어 구분) + 1

     예: words가 '10 20 30'이면 결과는 4
     -->
    <xsl:template name="title.lines">
        <xsl:param name="words" select="''"/>
        <xsl:param name="count" select="1"/>

        <xsl:choose>
            <xsl:when test="string-length($words) = 0">
                <xsl:value-of select="$count"/>
            </xsl:when>
            <xsl:when test="contains($words, ' ')">
                <xsl:call-template name="title.lines">
                    <xsl:with-param name="words" select="substring-after($words, ' ')"/>
                    <xsl:with-param name="count" select="$count + 1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$count + 1"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
     Parameters
        * title: 문자열
        * breaks: title을 나눌 위치 (문자 기준)
     Return
        * breaks가 null이면 title을 포함하는 하나의 fo:block
        * 그렇지 않으면 breaks에서 지정한 위치에서 title을 나누어 감싼 fo:block들

     예: title이 "JEUS Web Container 안내서"이고, breaks가 "9 19"이면
         title을 9번째, 19번째 문자에서 나누어 ["JEUS Web", "Container", "안내서"]와 같이 만들고 각각을 fo:block으로 감싼다.
         즉 9번째, 19번째 문자에서 문서 제목을 줄바꿈 하여 출력하는 것과 같다.
         (이 때 9번째, 19번째 문자는 제거된다. 그러므로 해당 위치는 공백 문자여야 한다.)
     -->
    <xsl:template name="title.blocks">
        <xsl:param name="title" select="''"/>
        <xsl:param name="subtitle" select="''"/>
        <xsl:param name="titleabbrev" select="''"/>
        <xsl:param name="acknowledgements" select="''"/>
        <xsl:param name="breaks" select="''"/>

        <xsl:choose>
            <xsl:when test="$breaks = ''">
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$title.font.size}pt" font-weight="bold">
                    <xsl:value-of select="$title"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$subtitle.font.size}pt">
                    <xsl:value-of select="$subtitle"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$titleabbrev.font.size}pt" space-before="60pt">
                    <xsl:value-of select="$titleabbrev"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$acknowledgements.font.size}pt" font-weight="bold">
                    <xsl:value-of select="$acknowledgements"/>
                </fo:block>
            </xsl:when>
            <xsl:when test="contains($breaks, ' ')">
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$title.font.size}pt" font-weight="bold">
                    <xsl:value-of select="substring($title, 1, number(substring-before($breaks, ' '))-1)"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$subtitle.font.size}pt">
                    <xsl:value-of select="substring($subtitle, 1, number(substring-before($breaks, ' '))-1)"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$titleabbrev.font.size}pt" space-before="60pt">
                    <xsl:value-of select="substring($titleabbrev, 1, number(substring-before($breaks, ' '))-1)"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$acknowledgements.font.size}pt" font-weight="bold">
                    <xsl:value-of select="substring($acknowledgements, 1, number(substring-before($breaks, ' '))-1)"/>
                </fo:block>
                <xsl:call-template name="title.blocks">
                    <xsl:with-param name="title" select="substring($title, number(substring-before($breaks, ' '))+1)"/>
                    <xsl:with-param name="subtitle" select="substring($subtitle, number(substring-before($breaks, ' '))+1)"/>
                    <xsl:with-param name="titleabbrev" select="substring($titleabbrev, number(substring-before($breaks, ' '))+1)"/>
                    <xsl:with-param name="acknowledgements" select="substring($acknowledgements, number(substring-before($breaks, ' '))+1)"/>
                    <xsl:with-param name="breaks" select="substring-after($breaks, ' ')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$title.font.size}pt" font-weight="bold">
                    <xsl:value-of select="substring($title, 1, number($breaks)-1)"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$subtitle.font.size}pt">
                    <xsl:value-of select="substring($subtitle, 1, number($breaks)-1)"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$titleabbrev.font.size}pt" space-before="60pt">
                    <xsl:value-of select="substring($titleabbrev, 1, number($breaks)-1)"/>
                </fo:block>
                <fo:block text-align="right"
                          font-family="sans-serif" font-size="{$acknowledgements.font.size}pt" font-weight="bold">
                    <xsl:value-of select="substring($acknowledgements, 1, number($breaks)-1)"/>
                </fo:block>
                <xsl:call-template name="title.blocks">
                    <xsl:with-param name="title" select="substring($title, number($breaks)+1)"/>
                    <xsl:with-param name="subtitle" select="substring($subtitle, number($breaks)+1)"/>
                    <xsl:with-param name="titleabbrev" select="substring($titleabbrev, number($breaks)+1)"/>
                    <xsl:with-param name="acknowledgements" select="substring($acknowledgements, number($breaks)+1)"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="book.titlepage.recto">
        <!--
         <book> 내에 titlepage.title.breaks(dbfo)라는 processing instruction이 있으면
         그 값을 변수 pi.title.breaks에 대입한다.
         -->
        <xsl:variable name="pi.title.breaks">
            <xsl:call-template name="dbfo-attribute">
                <xsl:with-param name="pis" select="processing-instruction('dbfo')"/>
                <xsl:with-param name="attribute" select="'titlepage.title.breaks'"/>
            </xsl:call-template>
        </xsl:variable>

        <!--
         pi.title.breaks가 ''이면 1, 그렇지 않으면 pi.title.breaks에 포함된 단어 개수를 변수 title.lines에 대입한다.
         -->
        <xsl:variable name="title.lines">
            <xsl:call-template name="title.lines">
                <xsl:with-param name="words" select="$pi.title.breaks"/>
            </xsl:call-template>
        </xsl:variable>

        <!--
         문서 제목과 회사 로고 사이의 여백 높이를 계산하여 변수 space.before.logo에 대입한다.
         ($space.before.logo = 170 - $title.font.size * $title.lines * 0.5)
         이 때 다음과 같이 가정한다:
            * 1mm = 3pt
            * line-height=150%
         -->
        <xsl:variable name="space.before.logo0">
            <xsl:value-of select="$title.font.size * $title.lines"/>
        </xsl:variable>
        <xsl:variable name="space.before.logo1">
            <xsl:value-of select="$space.before.logo0 * 0.5"/>
        </xsl:variable>
        <xsl:variable name="space.before.logo">
            <xsl:value-of select="130 - $space.before.logo1"/>
        </xsl:variable>

        <!-- 문서 제목 위에 적당히 여백을 둔다. -->
        <fo:block space-after="8mm">
            <xsl:text>&#160;</xsl:text>
        </fo:block>

        <!-- pi.title.breaks에서 지정한 위치에서 줄바꿈 하여 문서 제목을 출력한다. -->
        <xsl:call-template name="title.blocks">
            <xsl:with-param name="title" select="title"/>
            <xsl:with-param name="subtitle" select="subtitle"/>
            <xsl:with-param name="titleabbrev" select="titleabbrev"/>
            <xsl:with-param name="acknowledgements" select="acknowledgements"/>
            <xsl:with-param name="breaks" select="$pi.title.breaks"/>
        </xsl:call-template>

        <!-- space.before.logo만큼 간격을 둔 뒤 회사 로고를 넣는다. -->
        <fo:block text-align="center" space-before="{$space.before.logo}mm">
            <fo:external-graphic src="url(file:{$logo.image})"/>
        </fo:block>

        <!-- 2mm 간격을 두고 저작권을 표시한다. -->
        <fo:block text-align="center" space-before="2mm" font-weight="bold">
            <xsl:text>Copyright &#xA9;</xsl:text>
            <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>
        </fo:block>
    </xsl:template>

    <xsl:template name="book.titlepage.verso">
        <fo:block font-weight="bold" xsl:use-attribute-sets="normal.para.spacing">
            <xsl:text>Copyright Notice</xsl:text>
        </fo:block>
        <fo:block xsl:use-attribute-sets="normal.para.spacing">
            <xsl:text>Copyright &#xA9;</xsl:text>
            <xsl:value-of select="bookinfo/copyright"/>
        </fo:block>
        <fo:block xsl:use-attribute-sets="normal.para.spacing">
            <xsl:value-of select="bookinfo/publisher/publishername"/>
        </fo:block>
        <fo:block xsl:use-attribute-sets="normal.para.spacing">
            <xsl:value-of select="bookinfo/publisher/address"/>
        </fo:block>

        <xsl:for-each select="bookinfo/legalnotice/*">
            <xsl:choose>
                <xsl:when test="name(.) = 'title'">
                    <fo:block space-before="1.5mm">
                        <xsl:text>&#160;</xsl:text>
                    </fo:block>
                    <fo:block font-weight="bold">
                        <xsl:value-of select="."/>
                    </fo:block>
                </xsl:when>
                <xsl:otherwise>
                    <fo:block space-before="1.5mm">
                        <xsl:value-of select="."/>
                    </fo:block>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>

        <fo:block font-weight="bold" space-before="5.5mm">
            <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'bookinfo-infotitle'"/>
            </xsl:call-template>
        </fo:block>
        <fo:block xsl:use-attribute-sets="normal.para.spacing">
            <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'bookinfo-title'"/>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="title"/><xsl:text>&#160;&#160;</xsl:text><xsl:value-of select="subtitle"/>
        </fo:block>
        <xsl:if test="bookinfo/pubdate">
            <fo:block xsl:use-attribute-sets="normal.para.spacing">
                <xsl:call-template name="gentext">
                    <xsl:with-param name="key" select="'bookinfo-pubdate'"/>
                </xsl:call-template>
                <xsl:text>: </xsl:text>
                <xsl:value-of select="bookinfo/pubdate"/>
            </fo:block>
        </xsl:if>
        <xsl:if test="bookinfo/productnumber">
            <fo:block xsl:use-attribute-sets="normal.para.spacing">
                <xsl:call-template name="gentext">
                    <xsl:with-param name="key" select="'bookinfo-productnumber'"/>
                </xsl:call-template>
                <xsl:text>: </xsl:text>
                <xsl:value-of select="bookinfo/productnumber"/>
            </fo:block>
        </xsl:if>
        <xsl:if test="bookinfo/edition">
            <fo:block xsl:use-attribute-sets="normal.para.spacing">
                <xsl:call-template name="gentext">
                    <xsl:with-param name="key" select="'bookinfo-edition'"/>
                </xsl:call-template>
                <xsl:text>: </xsl:text>
                <xsl:value-of select="bookinfo/edition"/>
            </fo:block>
        </xsl:if>
    </xsl:template>

    <xsl:template name="book.titlepage.separator"/>

    <xsl:template name="book.titlepage.before.verso">
        <fo:block break-before='page'/>
    </xsl:template>

</xsl:stylesheet>
