import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DetailedSector } from 'app/shared/model/detailed-sector.model';
import { DetailedSectorService } from './detailed-sector.service';
import { DetailedSectorComponent } from './detailed-sector.component';
import { DetailedSectorDetailComponent } from './detailed-sector-detail.component';
import { DetailedSectorUpdateComponent } from './detailed-sector-update.component';
import { DetailedSectorDeletePopupComponent } from './detailed-sector-delete-dialog.component';
import { IDetailedSector } from 'app/shared/model/detailed-sector.model';

@Injectable({ providedIn: 'root' })
export class DetailedSectorResolve implements Resolve<IDetailedSector> {
    constructor(private service: DetailedSectorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((detailedSector: HttpResponse<DetailedSector>) => detailedSector.body));
        }
        return of(new DetailedSector());
    }
}

export const detailedSectorRoute: Routes = [
    {
        path: 'detailed-sector',
        component: DetailedSectorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'detailed-sector/:id/view',
        component: DetailedSectorDetailComponent,
        resolve: {
            detailedSector: DetailedSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'detailed-sector/new',
        component: DetailedSectorUpdateComponent,
        resolve: {
            detailedSector: DetailedSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'detailed-sector/:id/edit',
        component: DetailedSectorUpdateComponent,
        resolve: {
            detailedSector: DetailedSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const detailedSectorPopupRoute: Routes = [
    {
        path: 'detailed-sector/:id/delete',
        component: DetailedSectorDeletePopupComponent,
        resolve: {
            detailedSector: DetailedSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'DetailedSectors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
