package com.assessment.cvs.common

import org.jsoup.Jsoup

fun String.removeHtmlTags(): String {
    return Jsoup.parse(this).text()
}