package com.example.first_multiplatformapp.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.sqrt


data class DonutItem(
    val category: String,
    val amount: Double,
    val color: Color
)


@Composable
fun DonutChart(
    items: List<DonutItem>,
    modifier: Modifier = Modifier
) {

    val total = items.sumOf { it.amount }

    // Какая категория сейчас выбрана
    var selectedItem by remember {
        mutableStateOf<DonutItem?>(null)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .pointerInput(items) {

                    awaitPointerEventScope {

                        while (true) {

                            val event = awaitPointerEvent()

                            if (
                                event.type ==
                                PointerEventType.Press
                            ) {

                                val position =
                                    event.changes
                                        .first()
                                        .position

                                // Центр графика
                                val center = Offset(
                                    x = size.width / 2f,
                                    y = size.height / 2f
                                )

                                // Расстояние от центра
                                val dx =
                                    position.x - center.x

                                val dy =
                                    position.y - center.y

                                val distance =
                                    sqrt(
                                        dx * dx +
                                                dy * dy
                                    )

                                val strokeWidth = 80f

                                val diameter =
                                    minOf(
                                        size.width,
                                        size.height
                                    ) - strokeWidth

                                val radius =
                                    diameter / 2f

                                val innerRadius =
                                    radius -
                                            strokeWidth / 2f

                                val outerRadius =
                                    radius +
                                            strokeWidth / 2f

                                // Проверяем,
                                // попал ли пользователь
                                // именно в кольцо
                                if (
                                    distance >= innerRadius &&
                                    distance <= outerRadius
                                ) {

                                    // Получаем угол точки
                                    var angle =
                                        (
                                                atan2(
                                                    dy.toDouble(),
                                                    dx.toDouble()
                                                ) * 180.0 / kotlin.math.PI
                                                ).toFloat()

                                    // Начинаем отсчёт
                                    // сверху
                                    angle += 90f

                                    if (angle < 0f) {
                                        angle += 360f
                                    }

                                    var currentAngle = 0f

                                    // Ищем сектор
                                    for (item in items) {

                                        val sweepAngle =
                                            (
                                                    item.amount /
                                                            total *
                                                            360.0
                                                    ).toFloat()

                                        if (
                                            angle >= currentAngle &&
                                            angle <=
                                            currentAngle +
                                            sweepAngle
                                        ) {

                                            selectedItem = item

                                            break
                                        }

                                        currentAngle +=
                                            sweepAngle
                                    }
                                }
                            }
                        }
                    }
                }
        ) {

            if (total <= 0) {
                return@Canvas
            }

            val strokeWidth = 80f

            val diameter =
                minOf(
                    size.width,
                    size.height
                ) - strokeWidth

            val topLeft = Offset(
                x = (size.width - diameter) / 2,
                y = (size.height - diameter) / 2
            )

            var startAngle = -90f

            items.forEach { item ->

                val sweepAngle =
                    (
                            item.amount /
                                    total *
                                    360.0
                            ).toFloat()

                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(
                        diameter,
                        diameter
                    ),
                    style = Stroke(
                        width = strokeWidth
                    )
                )

                startAngle += sweepAngle
            }
        }


        // =========================
        // ЦЕНТР ГРАФИКА
        // =========================

        if (selectedItem == null) {

            // Ничего не выбрано

            Text(
                text = "${total.toInt()} ₴",
                fontSize = 24.sp
            )

        } else {

            // Пользователь выбрал категорию

            val item = selectedItem!!

            val percent =
                if (total > 0) {
                    item.amount /
                            total *
                            100.0
                } else {
                    0.0
                }

            Card(
                modifier = Modifier
                    .padding(30.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp,
                            vertical = 14.dp
                        )
                ) {

                    Text(
                        text = item.category,
                        fontSize = 20.sp
                    )

                    Text(
                        text =
                            "${item.amount.toInt()} ₴",
                        fontSize = 24.sp
                    )

                    Text(
                        text =
                            "${percent.toInt()}% від усіх витрат",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}