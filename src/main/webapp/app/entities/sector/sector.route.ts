import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SectorComponent } from './sector.component';
import { SectorDetailComponent } from './sector-detail.component';
import { SectorPopupComponent } from './sector-dialog.component';
import { SectorDeletePopupComponent } from './sector-delete-dialog.component';

export const sectorRoute: Routes = [
    {
        path: 'sector',
        component: SectorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sectors'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sector/:id',
        component: SectorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sectors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sectorPopupRoute: Routes = [
    {
        path: 'sector-new',
        component: SectorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sector/:id/edit',
        component: SectorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sector/:id/delete',
        component: SectorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
