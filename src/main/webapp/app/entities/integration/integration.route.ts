import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { IntegrationComponent } from './integration.component';
import { IntegrationDetailComponent } from './integration-detail.component';
import { IntegrationPopupComponent } from './integration-dialog.component';
import { IntegrationDeletePopupComponent } from './integration-delete-dialog.component';

export const integrationRoute: Routes = [
    {
        path: 'integration',
        component: IntegrationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Integrations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'integration/:id',
        component: IntegrationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Integrations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const integrationPopupRoute: Routes = [
    {
        path: 'integration-new',
        component: IntegrationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Integrations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'integration/:id/edit',
        component: IntegrationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Integrations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'integration/:id/delete',
        component: IntegrationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Integrations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
