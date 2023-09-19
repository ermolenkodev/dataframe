package org.jetbrains.kotlinx.dataframe.impl.columns

import org.jetbrains.kotlinx.dataframe.api.ColumnsSelectionDsl
import org.jetbrains.kotlinx.dataframe.api.colsInternal
import org.jetbrains.kotlinx.dataframe.api.singleInternal
import org.jetbrains.kotlinx.dataframe.columns.ColumnResolutionContext
import org.jetbrains.kotlinx.dataframe.columns.ColumnSet
import org.jetbrains.kotlinx.dataframe.columns.ColumnWithPath
import org.jetbrains.kotlinx.dataframe.columns.ColumnsResolver
import org.jetbrains.kotlinx.dataframe.columns.SingleColumn

/**
 * ## Transformable ColumnSet
 * This type of [ColumnsResolver] can be [transformed][transformResolve] before being resolved.
 *
 * This is especially useful for calls like
 * [colsInternal { }][ColumnsResolver.colsInternal].[atAnyDepthImpl()][atAnyDepthImpl],
 * where [atAnyDepthImpl][atAnyDepthImpl] modifies the [ColumnSet][ColumnsResolver]
 * that [colsInternal { }][ColumnsResolver.colsInternal] operates on to include ALL columns, including those inside
 * column groups, before it's evaluated.
 *
 * @see [ColumnsResolver]
 * @see [TransformableSingleColumn]
 * @see [SingleColumn]
 */
public interface TransformableColumnSet<out C> : ColumnSet<C> {
    public fun transformResolve(
        context: ColumnResolutionContext,
        transformer: ColumnsResolverTransformer,
    ): List<ColumnWithPath<C>>
}

/**
 * ## Transformable SingleColumn
 * This type of [SingleColumn] can be [transformed][transformResolveSingle] before being resolved.
 *
 * This is especially useful for calls like
 * [singleInternal { }][ColumnsResolver.singleInternal].[atAnyDepthImpl()][atAnyDepthImpl],
 * where [atAnyDepthImpl][atAnyDepthImpl] modifies the [SingleColumn]
 * that [singleInternal { }][ColumnsResolver.singleInternal] operates on to include ALL columns, including those inside
 * column groups, before it's evaluated.
 *
 * @see [SingleColumn]
 * @see [TransformableColumnSet]
 * @see [ColumnsResolver]
 */
public interface TransformableSingleColumn<out C> : SingleColumn<C> {
    public fun transformResolveSingle(
        context: ColumnResolutionContext,
        transformer: ColumnsResolverTransformer,
    ): ColumnWithPath<C>?
}

/**
 * ## Columns Resolver Transformer.
 * This contains implementations for both [transform][ColumnSet.transform] and
 * [transformSingle][SingleColumn.transformSingle] and can be passed around.
 */
public interface ColumnsResolverTransformer {
    public fun transform(columnsResolver: ColumnsResolver<*>): ColumnsResolver<*>

    public fun transformSet(columnSet: ColumnSet<*>): ColumnsResolver<*>

    public fun transformSingle(singleColumn: SingleColumn<*>): ColumnsResolver<*>
}
