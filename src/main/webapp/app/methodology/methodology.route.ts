import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { MethodologyComponent } from './';

export const METHODOLOGY_ROUTE: Route = {
    path: 'methodology',
    component: MethodologyComponent,
    data: {
        authorities: [],
        pageTitle: 'Methodology & Assumptions'
    },
    canActivate: [UserRouteAccessService]
};
