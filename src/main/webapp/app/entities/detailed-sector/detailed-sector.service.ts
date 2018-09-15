import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDetailedSector } from 'app/shared/model/detailed-sector.model';

type EntityResponseType = HttpResponse<IDetailedSector>;
type EntityArrayResponseType = HttpResponse<IDetailedSector[]>;

@Injectable({ providedIn: 'root' })
export class DetailedSectorService {
    private resourceUrl = SERVER_API_URL + 'api/detailed-sectors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/detailed-sectors';

    constructor(private http: HttpClient) {}

    create(detailedSector: IDetailedSector): Observable<EntityResponseType> {
        return this.http.post<IDetailedSector>(this.resourceUrl, detailedSector, { observe: 'response' });
    }

    update(detailedSector: IDetailedSector): Observable<EntityResponseType> {
        return this.http.put<IDetailedSector>(this.resourceUrl, detailedSector, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDetailedSector>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDetailedSector[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDetailedSector[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
