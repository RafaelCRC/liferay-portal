/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {PagesVisitor} from 'dynamic-data-mapping-form-renderer/js/util/visitors.es';

import {updateRulesReferences} from '../util/rules.es';
import {
	updateFocusedField,
	updateSettingsContextProperty
} from '../util/settingsContext.es';

export const updatePages = (
	editingLanguageId,
	pages,
	previousFieldName,
	newFieldName,
	propertyName,
	propertyValue
) => {
	let parentFieldName;
	const visitor = new PagesVisitor(pages);

	let newPages = visitor.mapFields(
		(field, fieldIndex, columnIndex, rowIndex, pageIndex, parentField) => {
			if (field.fieldName === previousFieldName) {
				if (parentField) {
					parentFieldName = parentField.fieldName;
				}

				return {
					...field,
					fieldName: newFieldName,
					name: newFieldName,
					[propertyName]: propertyValue
				};
			}

			return field;
		},
		true,
		true
	);

	if (parentFieldName && previousFieldName !== newFieldName) {
		visitor.setPages(newPages);

		newPages = visitor.mapFields(
			field => {
				if (parentFieldName === field.fieldName) {
					const visitor = new PagesVisitor([{rows: field.rows}]);

					const layout = visitor.mapColumns(column => {
						return {
							...column,
							fields: column.fields.map(fieldName => {
								if (fieldName === previousFieldName) {
									return newFieldName;
								}

								return fieldName;
							})
						};
					});

					const {rows} = layout[0];

					return {
						...field,
						rows,
						settingsContext: updateSettingsContextProperty(
							editingLanguageId,
							field.settingsContext,
							'rows',
							rows
						)
					};
				}

				return field;
			},
			true,
			true
		);
	}

	return newPages;
};

export const updateField = (props, state, propertyName, propertyValue) => {
	const {editingLanguageId} = props;
	const {focusedField, pages, rules} = state;
	const {fieldName: previousFieldName} = focusedField;
	const newFocusedField = updateFocusedField(
		props,
		state,
		propertyName,
		propertyValue
	);
	const {fieldName: newFieldName} = newFocusedField;

	const newPages = updatePages(
		editingLanguageId,
		pages,
		previousFieldName,
		newFieldName,
		propertyName,
		newFocusedField[propertyName]
	);

	return {
		focusedField: newFocusedField,
		pages: newPages,
		rules: updateRulesReferences(rules || [], focusedField, newFocusedField)
	};
};

export const handleFieldEdited = (props, state, event) => {
	const {propertyName, propertyValue} = event;
	let newState = {};

	if (propertyName !== 'name' || propertyValue !== '') {
		newState = updateField(props, state, propertyName, propertyValue);
	}

	return newState;
};

export default handleFieldEdited;
