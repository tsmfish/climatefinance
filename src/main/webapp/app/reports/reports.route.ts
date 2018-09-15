import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { ReportsComponent } from './';

export const REPORTS_ROUTE: Route = {
    path: 'reports',
    component: ReportsComponent,
    data: {
        authorities: [],
        pageTitle: 'Pacific Climate Change Tracking Tool'
    },
    canActivate: [UserRouteAccessService]
};
