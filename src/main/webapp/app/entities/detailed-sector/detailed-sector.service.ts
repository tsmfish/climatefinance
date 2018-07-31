import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { DetailedSector } from './detailed-sector.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DetailedSector>;

@Injectable()
export class DetailedSectorService {

    private resourceUrl =  SERVER_API_URL + 'api/detailed-sectors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/detailed-sectors';

    constructor(private http: HttpClient) { }

    create(detailedSector: DetailedSector): Observable<EntityResponseType> {
        const copy = this.convert(detailedSector);
        return this.http.post<DetailedSector>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(detailedSector: DetailedSector): Observable<EntityResponseType> {
        const copy = this.convert(detailedSector);
        return this.http.put<DetailedSector>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DetailedSector>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DetailedSector[]>> {
        const options = createRequestOption(req);
        return this.http.get<DetailedSector[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DetailedSector[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<DetailedSector[]>> {
        const options = createRequestOption(req);
        return this.http.get<DetailedSector[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DetailedSector[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DetailedSector = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DetailedSector[]>): HttpResponse<DetailedSector[]> {
        const jsonResponse: DetailedSector[] = res.body;
        const body: DetailedSector[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DetailedSector.
     */
    private convertItemFromServer(detailedSector: DetailedSector): DetailedSector {
        const copy: DetailedSector = Object.assign({}, detailedSector);
        return copy;
    }

    /**
     * Convert a DetailedSector to a JSON which can be sent to the server.
     */
    private convert(detailedSector: DetailedSector): DetailedSector {
        const copy: DetailedSector = Object.assign({}, detailedSector);
        return copy;
    }
}
