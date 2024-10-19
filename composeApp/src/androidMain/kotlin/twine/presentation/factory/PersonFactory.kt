package twine.presentation.factory

import android.graphics.PointF
import twine.presentation.data.BodyPart
import twine.presentation.data.KeyPoint
import twine.presentation.data.Person
import twine.presentation.model.TypeTraining
import kotlin.math.atan2
import kotlin.math.hypot

class PersonFactory {

    fun createPerson(
        keyPoints: MutableList<KeyPoint>,
        typeTraining: TypeTraining,
        percentOfModelPrediction: Float
    ): Person {

        //TODO refactor it depends on typeTraining
        val leftHip = keyPoints.find { it.bodyPart.position == BodyPart.LEFT_HIP.position }
        val rightHip = keyPoints.find { it.bodyPart.position == BodyPart.RIGHT_HIP.position }
        val rightKnee = keyPoints.find { it.bodyPart.position == BodyPart.RIGHT_KNEE.position }
        val leftKnee = keyPoints.find { it.bodyPart.position == BodyPart.LEFT_KNEE.position }
        // рассчет наклона корпуса при продольном шпагате
        val leftShoulder = keyPoints.find { it.bodyPart.position == BodyPart.LEFT_SHOULDER.position }
        val rightShoulder = keyPoints.find { it.bodyPart.position == BodyPart.RIGHT_SHOULDER.position }

        val leftPartBody = getAngle(leftKnee!!, leftHip!!, leftShoulder!!)
        val rightPartBody = getAngle(leftKnee!!, leftHip!!, leftShoulder!!)

        // рассчет для угла продольного шапагата
        val dx = leftHip?.coordinate!!.x - rightHip?.coordinate?.x!!
        val dy = leftHip?.coordinate!!.y - rightHip?.coordinate?.y!!
        val length = hypot(dx, dy)

        val newPoint =
            computePerpendicularPoints(
                point1 = leftHip.coordinate,
                point2 = rightHip.coordinate,
                distance = 50f
            )

        val leftAngleLongitudinalKeyPoint =
            KeyPoint(BodyPart.LEFT_PERPENDICULAR_POINT, coordinate = newPoint.first, score = 1f)
        val rightAngleLongitudinalKeyPoint =
            KeyPoint(BodyPart.RIGHT_PERPENDICULAR_POINT, coordinate = newPoint.second, score = 1f)

        val leftAngleLongitudinal = getAngle(leftKnee!!, leftHip!!, leftAngleLongitudinalKeyPoint)
        val rightAngleLongitudinal = getAngle(rightKnee!!, rightHip!!, rightAngleLongitudinalKeyPoint)

        keyPoints.add(leftAngleLongitudinalKeyPoint)
        keyPoints.add(rightAngleLongitudinalKeyPoint)

        val rightAngle = if (leftKnee != null && rightKnee != null && leftHip != null && rightHip != null) getAngle(
            leftKnee,
            leftHip,
            rightHip
        ) else null

        val leftAngle = if (leftKnee != null && rightKnee != null && leftHip != null && rightHip != null) getAngle(
            rightKnee,
            rightHip,
            leftHip
        ) else null

        return Person(
            keyPoints = keyPoints,
            score = percentOfModelPrediction,
            rightAngle = rightAngle,
            leftAngle = leftAngle,
            rightAngleLongitudinal = rightAngleLongitudinal,
            leftAngleLongitudinal = leftAngleLongitudinal,
            leftPartBody = leftPartBody,
            rightPartBody = rightPartBody
        )
    }

    private fun getAngle(firstPoint: KeyPoint, midPoint: KeyPoint, lastPoint: KeyPoint): Double {
        var result = Math.toDegrees(
            atan2(
                lastPoint.coordinate.y - midPoint.coordinate.y,
                lastPoint.coordinate.x - midPoint.coordinate.x
            )
                - atan2(
                firstPoint.coordinate.y - midPoint.coordinate.y.toDouble(),
                firstPoint.coordinate.x - midPoint.coordinate.x.toDouble()
            )
        )
        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

    /**
     * Вычисляет точки 5 и 6, которые являются перпендикулярными к точкам 1 и 2 соответственно.
     *
     * @param point1 Координаты точки 1.
     * @param point2 Координаты точки 2.
     * @param distance Расстояние от точек 1 и 2 до новых точек 5 и 6.
     * @return Пара точек 5 и 6.
     */
    private fun computePerpendicularPoints(
        point1: PointF,
        point2: PointF,
        distance: Float
    ): Pair<PointF, PointF> {
        val vx = point2.x - point1.x
        val vy = point2.y - point1.y

        // Длина вектора между точками 1 и 2
        val length = hypot(vx.toDouble(), vy.toDouble()).toFloat()
        if (length == 0f) {
            // Если точки совпадают, невозможно вычислить перпендикулярные точки
            return Pair(point1, point2)
        }

        // Нормализованный вектор, перпендикулярный вектору (vx, vy)
        val ux = -vy / length
        val uy = vx / length

        // Вычисляем точку 5, перпендикулярную к точке 1
        val point5 = PointF(
            point1.x + ux * distance,
            point1.y + uy * distance
        )

        // Вычисляем точку 6, перпендикулярную к точке 2
        val point6 = PointF(
            point2.x + ux * distance,
            point2.y + uy * distance
        )

        return Pair(point5, point6)
    }
}