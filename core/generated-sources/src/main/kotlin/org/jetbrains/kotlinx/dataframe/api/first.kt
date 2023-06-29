package org.jetbrains.kotlinx.dataframe.api

import org.jetbrains.kotlinx.dataframe.ColumnFilter
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.RowFilter
import org.jetbrains.kotlinx.dataframe.api.FirstColumnsSelectionDsl.CommonFirstDocs.Examples
import org.jetbrains.kotlinx.dataframe.columns.ColumnGroup
import org.jetbrains.kotlinx.dataframe.columns.ColumnPath
import org.jetbrains.kotlinx.dataframe.columns.ColumnReference
import org.jetbrains.kotlinx.dataframe.columns.ColumnSet
import org.jetbrains.kotlinx.dataframe.columns.SingleColumn
import org.jetbrains.kotlinx.dataframe.columns.asColumnSet
import org.jetbrains.kotlinx.dataframe.columns.size
import org.jetbrains.kotlinx.dataframe.columns.values
import org.jetbrains.kotlinx.dataframe.impl.columns.TransformableColumnSet
import org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn
import org.jetbrains.kotlinx.dataframe.impl.columns.singleOrNullWithTransformerImpl
import org.jetbrains.kotlinx.dataframe.impl.columns.transform
import org.jetbrains.kotlinx.dataframe.nrow
import kotlin.reflect.KProperty

// region DataColumn

public fun <T> DataColumn<T>.first(): T = get(0)

public fun <T> DataColumn<T>.firstOrNull(): T? = if (size > 0) first() else null

public fun <T> DataColumn<T>.first(predicate: (T) -> Boolean): T = values.first(predicate)

public fun <T> DataColumn<T>.firstOrNull(predicate: (T) -> Boolean): T? = values.firstOrNull(predicate)

// endregion

// region DataFrame

public fun <T> DataFrame<T>.first(): DataRow<T> {
    if (nrow == 0) {
        throw NoSuchElementException("DataFrame has no rows. Use `firstOrNull`.")
    }
    return get(0)
}

public fun <T> DataFrame<T>.firstOrNull(): DataRow<T>? = if (nrow > 0) first() else null

public fun <T> DataFrame<T>.first(predicate: RowFilter<T>): DataRow<T> = rows().first { predicate(it, it) }

public fun <T> DataFrame<T>.firstOrNull(predicate: RowFilter<T>): DataRow<T>? = rows().firstOrNull { predicate(it, it) }

// endregion

// region GroupBy

public fun <T, G> GroupBy<T, G>.first(): ReducedGroupBy<T, G> = reduce { firstOrNull() }

public fun <T, G> GroupBy<T, G>.first(predicate: RowFilter<G>): ReducedGroupBy<T, G> = reduce { firstOrNull(predicate) }

// endregion

// region Pivot

public fun <T> Pivot<T>.first(): ReducedPivot<T> = reduce { firstOrNull() }

public fun <T> Pivot<T>.first(predicate: RowFilter<T>): ReducedPivot<T> = reduce { firstOrNull(predicate) }

// endregion

// region PivotGroupBy

public fun <T> PivotGroupBy<T>.first(): ReducedPivotGroupBy<T> = reduce { firstOrNull() }

public fun <T> PivotGroupBy<T>.first(predicate: RowFilter<T>): ReducedPivotGroupBy<T> = reduce { firstOrNull(predicate) }

// endregion

// region ColumnsSelectionDsl

public interface FirstColumnsSelectionDsl {

    /**
     * ## First (Child)
     * Returns the first ([transformable][TransformableSingleColumn]) column in this [ColumnSet] or [ColumnGroup]
     * that adheres to the given [condition\].
     * If no column adheres to the given [condition\], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][DataFrame.select]` { "myColumnGroup".`[firstChild][String.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * {@includeArg [Examples]}
     *
     * @param [condition\] The optional [ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][TransformableSingleColumn]) [SingleColumn] containing the first column
     *   that adheres to the given [condition\].
     * @throws [NoSuchElementException\] if no column adheres to the given [condition\].
     * @see [ColumnsSelectionDsl.last\]
     */
    private interface CommonFirstDocs {

        /** Examples key */
        interface Examples
    }

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { `[colsOf][SingleColumn.colsOf]`<`[String][String]`>().`[first][ColumnSet.first]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * `df.`[select][DataFrame.select]` { `[colsOf][SingleColumn.colsOf]`<`[Int][Int]`>().`[first][ColumnSet.first]`() }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    @Suppress("UNCHECKED_CAST")
    public fun <C> ColumnSet<C>.first(condition: ColumnFilter<C> = { true }): TransformableSingleColumn<C> =
        (allColumnsInternal() as TransformableColumnSet<C>)
            .transform { listOf(it.first(condition)) }
            .singleOrNullWithTransformerImpl()

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    public fun ColumnsSelectionDsl<*>.first(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        asSingleColumn().firstChild(condition)

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { myColumnGroup.`[firstChild][SingleColumn.firstChild]`() }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    public fun SingleColumn<DataRow<*>>.firstChild(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        ensureIsColGroup().asColumnSet().first(condition)

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { "myColumnGroup".`[firstChild][String.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    public fun String.firstChild(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).firstChild(condition)

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { Type::myColumnGroup.`[asColumnGroup][KProperty.asColumnGroup]`().`[firstChild][SingleColumn.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * `df.`[select][DataFrame.select]` { `[colGroup][ColumnsSelectionDsl.colGroup]`(Type::myColumnGroup).`[firstChild][SingleColumn.firstChild]`() }`
     *
     * `df.`[select][DataFrame.select]` { DataSchemaType::myColumnGroup.`[firstChild][KProperty.firstChild]`() }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    public fun KProperty<*>.firstChild(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).firstChild(condition)

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { Type::myColumnGroup.`[asColumnGroup][KProperty.asColumnGroup]`().`[firstChild][SingleColumn.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * `df.`[select][DataFrame.select]` { `[colGroup][ColumnsSelectionDsl.colGroup]`(Type::myColumnGroup).`[firstChild][SingleColumn.firstChild]`() }`
     *
     * `df.`[select][DataFrame.select]` { DataSchemaType::myColumnGroup.`[firstChild][KProperty.firstChild]`() }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("firstKPropertyDataRow")
    public fun KProperty<DataRow<*>>.firstChild(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).firstChild(condition)

    /**
     * ## First (Child)
     * Returns the first ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) column in this [ColumnSet][org.jetbrains.kotlinx.dataframe.columns.ColumnSet] or [ColumnGroup][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup]
     * that adheres to the given [condition].
     * If no column adheres to the given [condition], [NoSuchElementException] is thrown.
     *
     * NOTE: For [column groups][org.jetbrains.kotlinx.dataframe.columns.ColumnGroup], `first` is named `firstChild` instead to avoid confusion.
     *
     * #### Examples:
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { `[first][ColumnsSelectionDsl.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("order") } }`
     *
     * `df.`[select][org.jetbrains.kotlinx.dataframe.DataFrame.select]` { "myColumnGroup".`[firstChild][kotlin.String.firstChild]` { it.`[name][org.jetbrains.kotlinx.dataframe.columns.ColumnReference.name]`().`[startsWith][String.startsWith]`("year") }.`[recursively][ColumnsSelectionDsl.recursively]`() }`
     *
     * #### Examples for this overload:
     *
     * `df.`[select][DataFrame.select]` { "pathTo"["myColumnGroup"].`[firstChild][ColumnPath.firstChild]` { it.`[name][ColumnReference.name]`().`[startsWith][String.startsWith]`("year") } }`
     *
     * @param [condition] The optional [ColumnFilter][org.jetbrains.kotlinx.dataframe.ColumnFilter] condition that the column must adhere to.
     * @return A ([transformable][org.jetbrains.kotlinx.dataframe.impl.columns.TransformableSingleColumn]) [SingleColumn][org.jetbrains.kotlinx.dataframe.columns.SingleColumn] containing the first column
     *   that adheres to the given [condition].
     * @throws [NoSuchElementException] if no column adheres to the given [condition].
     * @see [ColumnsSelectionDsl.last]
     */
    public fun ColumnPath.firstChild(condition: ColumnFilter<*> = { true }): TransformableSingleColumn<*> =
        columnGroup(this).firstChild(condition)
}

// endregion
