import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Sector } from './sector.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Sector>;

@Injectable()
export class SectorService {

    private resourceUrl =  SERVER_API_URL + 'api/sectors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sectors';

    constructor(private http: HttpClient) { }

    create(sector: Sector): Observable<EntityResponseType> {
        const copy = this.convert(sector);
        return this.http.post<Sector>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(sector: Sector): Observable<EntityResponseType> {
        const copy = this.convert(sector);
        return this.http.put<Sector>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Sector>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Sector[]>> {
        const options = createRequestOption(req);
        return this.http.get<Sector[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Sector[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Sector[]>> {
        const options = createRequestOption(req);
        return this.http.get<Sector[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Sector[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Sector = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Sector[]>): HttpResponse<Sector[]> {
        const jsonResponse: Sector[] = res.body;
        const body: Sector[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Sector.
     */
    private convertItemFromServer(sector: Sector): Sector {
        const copy: Sector = Object.assign({}, sector);
        return copy;
    }

    /**
     * Convert a Sector to a JSON which can be sent to the server.
     */
    private convert(sector: Sector): Sector {
        const copy: Sector = Object.assign({}, sector);
        return copy;
    }
}
