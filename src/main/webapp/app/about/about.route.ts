import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { AboutComponent } from './';

export const ABOUT_ROUTE: Route = {
    path: 'about',
    component: AboutComponent,
    data: {
        authorities: [],
        pageTitle: 'Pacific Climate Change Finance Tracking Tool'
    },
    canActivate: [UserRouteAccessService]
};
