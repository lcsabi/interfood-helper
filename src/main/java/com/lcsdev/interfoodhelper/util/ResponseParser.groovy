package com.lcsdev.interfoodhelper.util

import org.jsoup.Jsoup

def document = Jsoup.parse(htmlResponse)
def table = document.select('table.tapanyagtable')
def rows = table.select('tr')

def nutritionData = [:]
int counter = 1
Map<Integer, String> labelMap = Map.of(
        2, "Energia",
        3, "Zsir",
        5, "Szenhidrat",
        7, "Feherje")

rows.each { row ->
    if (counter > 7) {
        return
    }
    if (labelMap.containsKey(counter)) {
        def cells = row.select('td')
        if (cells.size() == 3) {
            def field = labelMap[counter]
            def value = cells[1].text().trim() // Use the third <td> element for the value
            nutritionData[field] = value
        }
    }
    counter++
}

def dishCodeAndDishName = document.select("span").first().text()
def dishName = dishCodeAndDishName.split(":")[1].trim().replace("*", "")
def calories = nutritionData['Energia']
def protein = nutritionData['Feherje']
def fat = nutritionData['Zsir']
def carbs = nutritionData['Szenhidrat']

// Bind Groovy variables so they are accessible in Java
binding.setVariable("dishName", dishName)
binding.setVariable("calories", calories)
binding.setVariable("protein", protein)
binding.setVariable("fat", fat)
binding.setVariable("carbs", carbs)
