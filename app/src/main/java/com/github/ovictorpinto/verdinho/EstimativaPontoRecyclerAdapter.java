package com.github.ovictorpinto.verdinho;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ovictorpinto.verdinho.to.Estimativa;
import com.github.ovictorpinto.verdinho.to.LinhaTO;
import com.github.ovictorpinto.verdinho.to.PontoTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.tcsistemas.common.date.DataHelper;
import br.com.tcsistemas.common.string.StringHelper;

/**
 * Created by Suleiman on 14-04-2015.
 */
public class EstimativaPontoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 10;
    private static final int ITEM = 11;

    Context context;
    private List<Estimativa> estimativas;
    private long horarioDoServidor;
    private Map<Integer, LinhaTO> mapLinhas;
    private PontoTO pontoTO;

    public EstimativaPontoRecyclerAdapter(Context context, List<Estimativa> estimativas, long horarioDoServidor, Map<Integer, LinhaTO>
            mapLinhas, PontoTO pontoTO) {
        this.context = context;
        this.estimativas = estimativas;
        this.horarioDoServidor = horarioDoServidor;
        this.mapLinhas = mapLinhas;
        this.pontoTO = pontoTO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == HEADER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ly_header, viewGroup, false);
            return new HeadeHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ly_item_ponto, viewGroup, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        final Estimativa item = estimativas.get(position);
        if (item.getItinerarioId() == null) {
            return HEADER;
        } else {
            return ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final Estimativa item = estimativas.get(position);

        if (viewHolder.getItemViewType() == HEADER) {
            HeadeHolder itemViewHolder = (HeadeHolder) viewHolder;
            itemViewHolder.textviewNome.setText(item.getVeiculo());

        } else {

            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            TextView textView = itemViewHolder.textviewHorario;
            ImageView imageView = itemViewHolder.imageview;
            imageView.setVisibility(item.getAcessibilidade() ? View.VISIBLE : View.GONE);

            String hora = item.getHorarioText(context, horarioDoServidor);
            String horario = DataHelper.format(new Date(item.getHorarioNaOrigem()), "HH:mm");

            textView.setText(StringHelper.mergeSeparator(" - ", horario, hora));
            textView.setBackgroundResource(item.getBackgroundConfiabilidade(horarioDoServidor));

            LinhaTO linha = mapLinhas.get(item.getItinerarioId());
            itemViewHolder.textviewNumero.setText(linha.getIdentificadorLinhaFiltrado());
            itemViewHolder.textviewNome.setText(linha.getBandeira());
            itemViewHolder.mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, LinhaDetalheActivity.class);
                    i.putExtra(Estimativa.PARAM, item);
                    i.putExtra(PontoTO.PARAM, pontoTO);
                    i.putExtra(LinhaTO.PARAM, mapLinhas.get(item.getItinerarioId()));
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return estimativas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textviewHorario;
        TextView textviewNome;
        TextView textviewNumero;
        ImageView imageview;
        View mainView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textviewHorario = (TextView) itemView.findViewById(R.id.textview_horario);
            textviewNome = (TextView) itemView.findViewById(R.id.textview_nome_linha);
            textviewNumero = (TextView) itemView.findViewById(R.id.textview_numero_linha);
            imageview = (ImageView) itemView.findViewById(R.id.image_acessibilidade);
            mainView = itemView;
        }
    }

    class HeadeHolder extends RecyclerView.ViewHolder {
        TextView textviewNome;

        public HeadeHolder(View itemView) {
            super(itemView);
            textviewNome = (TextView) itemView.findViewById(R.id.textview_nome_linha);
        }
    }
}