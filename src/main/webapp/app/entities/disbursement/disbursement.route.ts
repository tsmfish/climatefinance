import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DisbursementComponent } from './disbursement.component';
import { DisbursementDetailComponent } from './disbursement-detail.component';
import { DisbursementPopupComponent } from './disbursement-dialog.component';
import { DisbursementDeletePopupComponent } from './disbursement-delete-dialog.component';

export const disbursementRoute: Routes = [
    {
        path: 'disbursement',
        component: DisbursementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'disbursement/:id',
        component: DisbursementDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const disbursementPopupRoute: Routes = [
    {
        path: 'disbursement-new',
        component: DisbursementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'disbursement/:id/edit',
        component: DisbursementPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'disbursement/:id/delete',
        component: DisbursementDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
