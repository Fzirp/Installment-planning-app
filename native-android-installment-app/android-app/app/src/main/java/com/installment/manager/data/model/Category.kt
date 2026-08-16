package com.installment.manager.data.model

data class Category(
    val name: String,
    val persianName: String,
    val icon: String,   // Material icon name reference
    val color: Long      // Color as ARGB long
)

object DefaultCategories {
    val list = listOf(
        Category("car", "خودرو", "directions_car", 0xFF4CAF50),
        Category("home", "مسکن", "home", 0xFF2196F3),
        Category("phone", "موبایل", "phone_android", 0xFF9C27B0),
        Category("appliance", "لوازم خانگی", "kitchen", 0xFFFF9800),
        Category("education", "تحصیلی", "school", 0xFF00BCD4),
        Category("medical", "پزشکی", "local_hospital", 0xFFF44336),
        Category("personal", "شخصی", "person", 0xFF607D8B),
        Category("bank", "وام بانکی", "account_balance", 0xFF795548),
        Category("shopping", "خرید", "shopping_cart", 0xFFE91E63),
        Category("other", "سایر", "more_horiz", 0xFF9E9E9E)
    )

    fun findByName(name: String): Category {
        return list.find { it.name == name } ?: list.last()
    }
}
