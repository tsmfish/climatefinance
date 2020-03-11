import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { MethodologyComponent, MethodologyEditComponent } from './';

export const METHODOLOGY_ROUTE: Route = {
    path: 'methodology',
    component: MethodologyComponent,
    data: {
        authorities: [],
        pageTitle: 'Methodology & Assumptions'
    },
    canActivate: [UserRouteAccessService]
};

export const METHODOLOGY_EDIT_ROUTE: Route = {
    path: 'methodology/edit',
    component: MethodologyEditComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Edit | Methodology & Assumptions'
    },
    canActivate: [UserRouteAccessService]
};
