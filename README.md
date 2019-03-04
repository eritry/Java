# expression.exceptions: Обработка ошибок

Модификации
 * *Базовая*
    * Класс `ExpressionParser` реализует интерфейс
        [Parser](java/expression/exceptions/Parser.java)
    * Классы `CheckedAdd`, `CheckedSubtract`, `CheckedMultiply`,
        `CheckedDivide` и `CheckedNegate` реализуют интерфейс
        [TripleExpression](java/expression/TripleExpression.java)
    * Не используются типы `long` и `double`
    * Не используются методы классов `Math` и `StrictMath`
    * [Исходный код тестов](java/expression/exceptions/ExceptionsTest.java)
 * *HighLow*
    * Дополнительно реализованы унарные операции:
        * `high` — значение, у которого оставлен только самый старший
          установленный бит `high -4` равно `Integer.MIN_VALUE`;
        * `low` — значение, у которого оставлен только самый младший
          установленный бит `low 18` равно `2`.
    * [Исходный код тестов](java/expression/exceptions/ExceptionsHighLowTest.java)
    
    
