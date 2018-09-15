import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { ChartsComponent } from './';

export const CHARTS_ROUTE: Route = {
    path: 'charts',
    component: ChartsComponent,
    data: {
        authorities: [],
        pageTitle: 'Charts and Analytics'
    },
    canActivate: [UserRouteAccessService]
};
