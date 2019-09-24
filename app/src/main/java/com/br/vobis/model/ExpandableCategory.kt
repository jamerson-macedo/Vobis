package com.br.vobis.model

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class ExpandableCategory(title: String?, items: MutableList<SubCategory>?) : ExpandableGroup<SubCategory>(title, items)