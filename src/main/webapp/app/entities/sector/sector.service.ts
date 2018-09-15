import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISector } from 'app/shared/model/sector.model';

type EntityResponseType = HttpResponse<ISector>;
type EntityArrayResponseType = HttpResponse<ISector[]>;

@Injectable({ providedIn: 'root' })
export class SectorService {
    private resourceUrl = SERVER_API_URL + 'api/sectors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sectors';

    constructor(private http: HttpClient) {}

    create(sector: ISector): Observable<EntityResponseType> {
        return this.http.post<ISector>(this.resourceUrl, sector, { observe: 'response' });
    }

    update(sector: ISector): Observable<EntityResponseType> {
        return this.http.put<ISector>(this.resourceUrl, sector, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISector>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISector[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISector[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
