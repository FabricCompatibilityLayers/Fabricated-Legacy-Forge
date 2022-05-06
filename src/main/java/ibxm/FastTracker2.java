package ibxm;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FastTracker2 {
    public FastTracker2() {
    }

    public static boolean is_xm(byte[] header_60_bytes) {
        String xm_identifier = ascii_text(header_60_bytes, 0, 17);
        return xm_identifier.equals("Extended Module: ");
    }

    public static Module load_xm(byte[] header_60_bytes, DataInput data_input) throws IOException {
        if (!is_xm(header_60_bytes)) {
            throw new IllegalArgumentException("Not an XM file!");
        } else {
            int xm_version = unsigned_short_le(header_60_bytes, 58);
            if (xm_version != 260) {
                throw new IllegalArgumentException("Sorry, XM version " + xm_version + " is not supported!");
            } else {
                Module module = new Module();
                module.song_title = ascii_text(header_60_bytes, 17, 20);
                String tracker_name = ascii_text(header_60_bytes, 38, 20);
                boolean delta_env = tracker_name.startsWith("DigiBooster Pro");
                byte[] structure_header = new byte[4];
                data_input.readFully(structure_header);
                int song_header_length = int_le(structure_header, 0);
                byte[] song_header = new byte[song_header_length];
                data_input.readFully(song_header, 4, song_header_length - 4);
                int sequence_length = unsigned_short_le(song_header, 4);
                module.restart_sequence_index = unsigned_short_le(song_header, 6);
                int num_channels = unsigned_short_le(song_header, 8);
                int num_patterns = unsigned_short_le(song_header, 10);
                int num_instruments = unsigned_short_le(song_header, 12);
                int xm_flags = unsigned_short_le(song_header, 14);
                module.linear_periods = (xm_flags & 1) == 1;
                module.global_volume = 64;
                module.channel_gain = 12288;
                module.default_speed = unsigned_short_le(song_header, 16);
                module.default_tempo = unsigned_short_le(song_header, 18);
                module.set_num_channels(num_channels);

                int idx;
                for(idx = 0; idx < num_channels; ++idx) {
                    module.set_initial_panning(idx, 128);
                }

                module.set_sequence_length(sequence_length);

                for(idx = 0; idx < sequence_length; ++idx) {
                    module.set_sequence(idx, song_header[20 + idx] & 255);
                }

                module.set_num_patterns(num_patterns);

                for(idx = 0; idx < num_patterns; ++idx) {
                    module.set_pattern(idx, read_xm_pattern(data_input, num_channels));
                }

                module.set_num_instruments(num_instruments);

                for(idx = 1; idx <= num_instruments; ++idx) {
                    try {
                        Instrument instrument = read_xm_instrument(data_input, delta_env);
                        module.set_instrument(idx, instrument);
                    } catch (EOFException var17) {
                        System.out.println("Instrument " + idx + " is missing!");
                    }
                }

                return module;
            }
        }
    }

    private static Pattern read_xm_pattern(DataInput data_input, int num_channels) throws IOException {
        byte[] structure_header = new byte[4];
        data_input.readFully(structure_header);
        int pattern_header_length = int_le(structure_header, 0);
        byte[] pattern_header = new byte[pattern_header_length];
        data_input.readFully(pattern_header, 4, pattern_header_length - 4);
        int packing_type = pattern_header[4];
        if (packing_type != 0) {
            throw new IllegalArgumentException("Pattern packing type " + packing_type + " is not supported!");
        } else {
            Pattern pattern = new Pattern();
            pattern.num_rows = unsigned_short_le(pattern_header, 5);
            int pattern_data_length = unsigned_short_le(pattern_header, 7);
            byte[] pattern_data = new byte[pattern_data_length];
            data_input.readFully(pattern_data);
            pattern.set_pattern_data(pattern_data);
            return pattern;
        }
    }

    private static Instrument read_xm_instrument(DataInput data_input, boolean delta_env) throws IOException {
        byte[] structure_header = new byte[4];
        data_input.readFully(structure_header);
        int instrument_header_length = int_le(structure_header, 0);
        byte[] instrument_header = new byte[instrument_header_length];
        data_input.readFully(instrument_header, 4, instrument_header_length - 4);
        Instrument instrument = new Instrument();
        instrument.name = ascii_text(instrument_header, 4, 22);
        int num_samples = unsigned_short_le(instrument_header, 27);
        if (num_samples > 0) {
            instrument.set_num_samples(num_samples);

            int idx;
            for(idx = 0; idx < 96; ++idx) {
                instrument.set_key_to_sample(idx + 1, instrument_header[33 + idx] & 255);
            }

            Envelope envelope = new Envelope();
            int env_num_points = instrument_header[225] & 255;
            envelope.set_num_points(env_num_points);

            int env_tick;
            int env_ampl;
            for(idx = 0; idx < env_num_points; ++idx) {
                env_tick = unsigned_short_le(instrument_header, 129 + idx * 4);
                env_ampl = unsigned_short_le(instrument_header, 131 + idx * 4);
                envelope.set_point(idx, env_tick, env_ampl, delta_env);
            }

            envelope.set_sustain_point(instrument_header[227] & 255);
            envelope.set_loop_points(instrument_header[228] & 255, instrument_header[229] & 255);
            int flags = instrument_header[233] & 255;
            instrument.volume_envelope_active = (flags & 1) == 1;
            envelope.sustain = (flags & 2) == 2;
            envelope.looped = (flags & 4) == 4;
            instrument.set_volume_envelope(envelope);
            envelope = new Envelope();
            env_num_points = instrument_header[226] & 255;
            envelope.set_num_points(env_num_points);

            for(idx = 0; idx < env_num_points; ++idx) {
                env_tick = unsigned_short_le(instrument_header, 177 + idx * 4);
                env_ampl = unsigned_short_le(instrument_header, 179 + idx * 4);
                envelope.set_point(idx, env_tick, env_ampl, delta_env);
            }

            envelope.set_sustain_point(instrument_header[230] & 255);
            envelope.set_loop_points(instrument_header[231] & 255, instrument_header[232] & 255);
            flags = instrument_header[234] & 255;
            instrument.panning_envelope_active = (flags & 1) == 1;
            envelope.sustain = (flags & 2) == 2;
            envelope.looped = (flags & 4) == 4;
            instrument.set_panning_envelope(envelope);
            instrument.vibrato_type = instrument_header[235] & 255;
            instrument.vibrato_sweep = instrument_header[236] & 255;
            instrument.vibrato_depth = instrument_header[237] & 255;
            instrument.vibrato_rate = instrument_header[238] & 255;
            instrument.volume_fade_out = unsigned_short_le(instrument_header, 239);
            byte[] sample_headers = new byte[num_samples * 40];
            data_input.readFully(sample_headers);

            for(idx = 0; idx < num_samples; ++idx) {
                instrument.set_sample(idx, read_xm_sample(sample_headers, idx, data_input));
            }
        }

        return instrument;
    }

    private static Sample read_xm_sample(byte[] sample_headers, int sample_idx, DataInput data_input) throws IOException {
        int header_offset = sample_idx * 40;
        Sample sample = new Sample();
        int sample_length = int_le(sample_headers, header_offset);
        int loop_start = int_le(sample_headers, header_offset + 4);
        int loop_length = int_le(sample_headers, header_offset + 8);
        sample.volume = sample_headers[header_offset + 12] & 255;
        int fine_tune = sample_headers[header_offset + 13];
        fine_tune = (fine_tune << 15) / 1536;
        sample.set_panning = true;
        int flags = sample_headers[header_offset + 14] & 255;
        if ((flags & 3) == 0) {
            loop_length = 0;
        }

        boolean ping_pong = (flags & 2) == 2;
        boolean sixteen_bit = (flags & 16) == 16;
        sample.panning = sample_headers[header_offset + 15] & 255;
        int relative_note = sample_headers[header_offset + 16];
        relative_note = (relative_note << 15) / 12;
        sample.transpose = relative_note + fine_tune;
        sample.name = ascii_text(sample_headers, header_offset + 18, 22);
        byte[] raw_sample_data = new byte[sample_length];

        try {
            data_input.readFully(raw_sample_data);
        } catch (EOFException var20) {
            System.out.println("Sample has been truncated!");
        }

        int in_idx = 0;
        int out_idx = 0;
        int last_sam = 0;
        short[] decoded_sample_data;
        int sam;
        if (sixteen_bit) {
            for(decoded_sample_data = new short[sample_length >> 1]; in_idx < raw_sample_data.length; ++out_idx) {
                sam = raw_sample_data[in_idx] & 255;
                sam |= (raw_sample_data[in_idx + 1] & 255) << 8;
                last_sam += sam;
                decoded_sample_data[out_idx] = (short)last_sam;
                in_idx += 2;
            }

            sample.set_sample_data(decoded_sample_data, loop_start >> 1, loop_length >> 1, ping_pong);
        } else {
            for(decoded_sample_data = new short[sample_length]; in_idx < raw_sample_data.length; ++out_idx) {
                sam = raw_sample_data[in_idx] & 255;
                last_sam += sam;
                decoded_sample_data[out_idx] = (short)(last_sam << 8);
                ++in_idx;
            }

            sample.set_sample_data(decoded_sample_data, loop_start, loop_length, ping_pong);
        }

        return sample;
    }

    private static int unsigned_short_le(byte[] buffer, int offset) {
        int value = buffer[offset] & 255;
        value |= (buffer[offset + 1] & 255) << 8;
        return value;
    }

    private static int int_le(byte[] buffer, int offset) {
        int value = buffer[offset] & 255;
        value |= (buffer[offset + 1] & 255) << 8;
        value |= (buffer[offset + 2] & 255) << 16;
        value |= (buffer[offset + 3] & 127) << 24;
        return value;
    }

    private static String ascii_text(byte[] buffer, int offset, int length) {
        byte[] string_buffer = new byte[length];

        for(int idx = 0; idx < length; ++idx) {
            int chr = buffer[offset + idx];
            if (chr < 32) {
                chr = 32;
            }

            string_buffer[idx] = (byte)chr;
        }

        String string;
        try {
            string = new String(string_buffer, 0, length, "ISO-8859-1");
        } catch (UnsupportedEncodingException var8) {
            string = "";
        }

        return string;
    }
}
