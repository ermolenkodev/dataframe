package org.jetbrains.kotlinx.dataframe.api

import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.api.FirstColumnsSelectionDsl.Usage.ColumnGroupName
import org.jetbrains.kotlinx.dataframe.api.FirstColumnsSelectionDsl.Usage.ColumnSetName
import org.jetbrains.kotlinx.dataframe.api.FirstColumnsSelectionDsl.Usage.PlainDslName
import org.jetbrains.kotlinx.dataframe.columns.*
import org.jetbrains.kotlinx.dataframe.columns.size
import org.jetbrains.kotlinx.dataframe.columns.values
import org.jetbrains.kotlinx.dataframe.documentation.Indent
import org.jetbrains.kotlinx.dataframe.impl.columns.TransformableColumnSet
import org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn
import org.jetbrains.kotlinx.dataframe.impl.columns.singleOrNullWithTransformerImpl
import org.jetbrains.kotlinx.dataframe.impl.columns.transform
import org.jetbrains.kotlinx.dataframe.nrow
import kotlin.reflect.KProperty

// region DataColumn

public fun <T> DataColumn<T>.last(): T = get(size - 1)

public fun <T> DataColumn<T>.lastOrNull(): T? = if (size > 0) last() else null

public fun <T> DataColumn<T>.last(predicate: (T) -> Boolean): T = values.last(predicate)

public fun <T> DataColumn<T>.lastOrNull(predicate: (T) -> Boolean): T? = values.lastOrNull(predicate)

// endregion

// region DataFrame

public fun <T> DataFrame<T>.lastOrNull(predicate: RowFilter<T>): DataRow<T>? =
    rowsReversed().firstOrNull { predicate(it, it) }

public fun <T> DataFrame<T>.last(predicate: RowFilter<T>): DataRow<T> = rowsReversed().first { predicate(it, it) }

public fun <T> DataFrame<T>.lastOrNull(): DataRow<T>? = if (nrow > 0) get(nrow - 1) else null

public fun <T> DataFrame<T>.last(): DataRow<T> {
    if (nrow == 0) {
        throw NoSuchElementException("DataFrame has no rows. Use `lastOrNull`.")
    }
    return get(nrow - 1)
}

// endregion

// region GroupBy

public fun <T, G> GroupBy<T, G>.last(): ReducedGroupBy<T, G> = reduce { lastOrNull() }

public fun <T, G> GroupBy<T, G>.last(predicate: RowFilter<G>): ReducedGroupBy<T, G> = reduce { lastOrNull(predicate) }

// endregion

// region Pivot

public fun <T> Pivot<T>.last(): ReducedPivot<T> = reduce { lastOrNull() }

public fun <T> Pivot<T>.last(predicate: RowFilter<T>): ReducedPivot<T> = reduce { lastOrNull(predicate) }

// endregion

// region PivotGroupBy

public fun <T> PivotGroupBy<T>.last(): ReducedPivotGroupBy<T> = reduce { lastOrNull() }

public fun <T> PivotGroupBy<T>.last(predicate: RowFilter<T>): ReducedPivotGroupBy<T> = reduce { lastOrNull(predicate) }

// endregion

// region ColumnsSelectionDsl

/**
 * See [Usage].
 */
public interface LastColumnsSelectionDsl {

    /**
     * ## Last (Col) Usage
     *
     * @include [ColumnsSelectionDsl.UsageTemplate]
     * {@setArg [ColumnsSelectionDsl.UsageTemplate.PlainDslFunctionsArg]
     *   {@include [PlainDslName]} {@include [ColumnsSelectionDsl.UsageTemplate.OptionalColumnFilter]}
     * }
     * {@setArg [ColumnsSelectionDsl.UsageTemplate.ColumnSetFunctionsArg]
     *   {@include [Indent]}{@include [ColumnSetName]} {@include [ColumnsSelectionDsl.UsageTemplate.OptionalColumnFilter]}
     * }
     * {@setArg [ColumnsSelectionDsl.UsageTemplate.ColumnGroupFunctionsArg]
     *   {@include [Indent]}{@include [ColumnGroupName]} {@include [ColumnsSelectionDsl.UsageTemplate.OptionalColumnFilter]}
     * }
     */
    public interface Usage {

        /** [last][ColumnsSelectionDsl.last] */
        public interface PlainDslName

        /** .[last][ColumnsSelectionDsl.last] */
        public interface ColumnSetName

        /** .[lastCol][ColumnsSelectionDsl.lastCol] */
        public interface ColumnGroupName
    }

    /**
     * ## Last (Col)
     * Returns the last ([transformable][TransformableSingleColumn]) column in this [ColumnSet] or [ColumnGroup]
     * that adheres to the given [condition\].
     * If no column adheres to the given [condition\], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][ColumnGroup], `last` is named `lastCol` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][DataFrame.select]` { `[last][ColumnsSelectionDsl.last]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][DataFrame.select]` { "myColumnGroup".`[lastCol][String.lastCol]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("order") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * {@getArg [Examples]}
     *
     * @param [condition\] The optional [ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][TransformableSingleColumn]) [SingleColumn] containing the last column
     *   that adheres to the given [condition\].
     * @throws [NoSuchElementException\] if no column adheres to the given [condition\].
     * @see [ColumnsSelectionDsl.first\]
     */
    private interface CommonLastDocs {

        /** Examples key */
        interface Examples
    }

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     * `df.`[select][DataFrame.select]` { `[colsOf][SingleColumn.colsOf]`<`[String][String]`>().`[last][ColumnSet.last]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * `df.`[select][DataFrame.select]` { `[colsOf][SingleColumn.colsOf]`<`[Int][Int]`>().`[last][ColumnSet.last]`() }`
     */
    @Suppress("UNCHECKED_CAST")
    public fun <C> ColumnSet<C>.last(condition: ColumnFilter<C> = { true }): TransformableSingleColumn<C> =
        (allColumnsInternal() as TransformableColumnSet<C>)
            .transform { listOf(it.last(condition)) }
            .singleOrNullWithTransformerImpl()

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     *
     * `df.`[select][DataFrame.select]` { `[last][ColumnsSelectionDsl.last]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     */
    public fun ColumnsSelectionDsl<*>.last(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        asSingleColumn().lastCol(condition)

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     *
     * `df.`[select][DataFrame.select]` { myColumnGroup.`[lastCol][SingleColumn.lastCol]`() }`
     */
    public fun SingleColumn<DataRow<*>>.lastCol(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        this.ensureIsColumnGroup().asColumnSet().last(condition)

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     * `df.`[select][DataFrame.select]` { "myColumnGroup".`[lastCol][String.lastCol]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     */
    public fun String.lastCol(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).lastCol(condition)

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     * `df.`[select][DataFrame.select]` { Type::myColumnGroup.`[asColumnGroup][KProperty.asColumnGroup]`().`[lastCol][SingleColumn.lastCol]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * `df.`[select][DataFrame.select]` { `[colGroup][ColumnsSelectionDsl.colGroup]`(Type::myColumnGroup).`[lastCol][SingleColumn.lastCol]`() }`
     *
     * `df.`[select][DataFrame.select]` { DataSchemaType::myColumnGroup.`[lastCol][KProperty.lastCol]`() }`
     */
    public fun KProperty<*>.lastCol(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).lastCol(condition)

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     * `df.`[select][DataFrame.select]` { Type::myColumnGroup.`[asColumnGroup][KProperty.asColumnGroup]`().`[lastCol][SingleColumn.lastCol]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * `df.`[select][DataFrame.select]` { `[colGroup][ColumnsSelectionDsl.colGroup]`(Type::myColumnGroup).`[lastCol][SingleColumn.lastCol]`() }`
     *
     * `df.`[select][DataFrame.select]` { DataSchemaType::myColumnGroup.`[lastCol][KProperty.lastCol]`() }`
     */
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("lastKPropertyDataRow")
    public fun KProperty<DataRow<*>>.lastCol(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).lastCol(condition)

    /**
     * @include [CommonLastDocs]
     * @setArg [CommonLastDocs.Examples]
     * `df.`[select][DataFrame.select]` { "pathTo"["myColumnGroup"].`[lastCol][ColumnPath.lastCol]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     */
    public fun ColumnPath.lastCol(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).lastCol(condition)
}

// endregion
