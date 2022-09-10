/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import SubscriptionsOverview from './components/SubscriptionsOverview';
import SupportOverview from './components/SupportOverview/';
import useCurrentKoroneikiAccount from './hooks/useCurrentKoroneikiAccount';

const Overview = () => {
	const {data, loading} = useCurrentKoroneikiAccount();
	const koroneikiAccount = data?.koroneikiAccountByExternalReferenceCode;

	return (
		<div>
			<SupportOverview
				koroneikiAccount={koroneikiAccount}
				loading={loading}
			/>

			<SubscriptionsOverview />
		</div>
	);
};

export default Overview;
