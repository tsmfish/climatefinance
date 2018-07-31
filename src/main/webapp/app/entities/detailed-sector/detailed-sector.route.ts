import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DetailedSectorComponent } from './detailed-sector.component';
import { DetailedSectorDetailComponent } from './detailed-sector-detail.component';
import { DetailedSectorPopupComponent } from './detailed-sector-dialog.component';
import { DetailedSectorDeletePopupComponent } from './detailed-sector-delete-dialog.component';

export const detailedSectorRoute: Routes = [
    {
        path: 'detailed-sector',
        component: DetailedSectorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'detailed-sector/:id',
        component: DetailedSectorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const detailedSectorPopupRoute: Routes = [
    {
        path: 'detailed-sector-new',
        component: DetailedSectorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'detailed-sector/:id/edit',
        component: DetailedSectorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'detailed-sector/:id/delete',
        component: DetailedSectorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
