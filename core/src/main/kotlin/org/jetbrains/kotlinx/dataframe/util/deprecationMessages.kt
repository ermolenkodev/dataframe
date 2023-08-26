package org.jetbrains.kotlinx.dataframe.util

/*
 * This file contains deprecation messages for the whole core module.
 * After each release, all messages should be reviewed and updated.
 * Level.WARNING -> Level.ERROR
 * Level.ERROR -> Remove
 */

// region WARNING in 0.11.0, ERROR in 0.12.0

private const val message_0_12_0 = "Was removed in 0.12.0."

internal const val DF_READ_DEPRECATION_MESSAGE = "Replaced with `unfold` operation. $message_0_12_0"
internal const val DF_READ_REPLACE_MESSAGE = "this.unfold(*columns)"

internal const val ITERABLE_COLUMNS_DEPRECATION_MESSAGE = "Replaced with `toColumnSet()` operation. $message_0_12_0"

// endregion

// region WARNING in 0.12.0, ERROR in 0.13.0

private const val message_0_13_0 = "Will be removed in 0.13.0."

internal const val DIFF_DEPRECATION_MESSAGE = "Replaced to explicitly indicate nullable return value; added a new non-null overload. $message_0_13_0"
internal const val DIFF_REPLACE_MESSAGE = "this.diffOrNull(expression)"
internal const val DIFF_OR_NULL_IMPORT = "org.jetbrains.kotlinx.dataframe.api.diffOrNull"

internal const val UPDATE_AS_NULLABLE_MESSAGE = "This function is useless unless in combination with `withValue(null)`, but then you can just use `with { null }`. $message_0_13_0"
internal const val UPDATE_AS_NULLABLE_REPLACE = "this as Update<T, C?>"

internal const val UPDATE_WITH_VALUE = "Replaced in favor of `with { value }`. $message_0_13_0"
internal const val UPDATE_WITH_VALUE_REPLACE = "this.with { value }"

internal const val COL_SELECT_DSL_GROUP = "Use `colGroup()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_GROUP_REPLACE = "this.colGroup(name)"

internal const val COLS_SELECT_DSL_GROUP = "Use `colGroups` instead. $message_0_13_0"
internal const val COLS_SELECT_DSL_GROUP_REPLACE = "this.colGroups(filter)"

internal const val COL_SELECT_DSL_ALL_COLS = "Use `allCols()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_COLS_REPLACE = "this.allCols()"

internal const val COL_SELECT_DSL_ALL_COLS_AFTER = "Use `allColsAfter()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_COLS_AFTER_REPLACE = "this.allColsAfter(name)"

internal const val COL_SELECT_DSL_ALL_COLS_BEFORE = "Use `allColsBefore()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_COLS_BEFORE_REPLACE = "this.allColsBefore(name)"

internal const val COL_SELECT_DSL_ALL_FROM = "Use `allFrom()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_FROM_REPLACE = "this.allFrom(name)"

internal const val COL_SELECT_DSL_ALL_COLS_FROM = "Use `allColsFrom()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_COLS_FROM_REPLACE = "this.allColsFrom(name)"

internal const val COL_SELECT_DSL_ALL_UP_TO = "Use `allUpTo()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_UP_TO_REPLACE = "this.allUpTo(name)"

internal const val COL_SELECT_DSL_ALL_COLS_UP_TO = "Use `allColsUpTo()` instead. $message_0_13_0"
internal const val COL_SELECT_DSL_ALL_COLS_UP_TO_REPLACE = "this.allColsUpTo(name)"
// endregion

// region WARNING in 0.13.0, ERROR in 0.14.0

private const val message_0_14_0 = "Will be removed in 0.14.0."

// endregion

// region keep across releases

internal const val IDENTITY_FUNCTION = "This overload is an identity function and can be omitted."

internal const val COL_REPLACE = "col"

// endregion
